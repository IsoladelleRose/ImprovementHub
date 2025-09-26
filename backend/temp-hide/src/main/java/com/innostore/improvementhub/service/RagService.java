package com.innostore.improvementhub.service;

import com.innostore.improvementhub.entity.Document;
import com.innostore.improvementhub.repository.DocumentRepository;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

// @Service
public class RagService {

    private static final Logger logger = LoggerFactory.getLogger(RagService.class);

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;
    private final ChatLanguageModel chatModel;
    private final DocumentRepository documentRepository;

    @Value("${langchain4j.open-ai.chat-model.api-key:}")
    private String openaiApiKey;

    @Autowired
    public RagService(DocumentRepository documentRepository, DataSource dataSource,
                     @Value("${langchain4j.open-ai.chat-model.api-key:}") String openaiApiKey) {
        this.documentRepository = documentRepository;
        this.openaiApiKey = openaiApiKey;

        // Initialize embedding model (runs locally - no API key needed)
        this.embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        logger.info("Initialized AllMiniLmL6V2EmbeddingModel");

        // Initialize PostgreSQL vector store
        this.embeddingStore = PgVectorEmbeddingStore.builder()
                .dataSource(dataSource)
                .table("langchain4j_embeddings")
                .dimension(384) // AllMiniLM produces 384-dimensional embeddings
                .build();
        logger.info("Initialized PgVector embedding store");

        // Initialize chat model (OpenAI)
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            this.chatModel = OpenAiChatModel.builder()
                    .apiKey(openaiApiKey)
                    .modelName("gpt-3.5-turbo")
                    .temperature(0.7)
                    .build();
            logger.info("Initialized OpenAI chat model");
        } else {
            this.chatModel = null;
            logger.warn("OpenAI API key not configured - chat functionality will be limited");
        }
    }

    /**
     * Ingest a document by splitting it into chunks and storing embeddings
     */
    public void ingestDocument(String title, String content) {
        try {
            logger.info("Starting document ingestion for: {}", title);

            // Save document to database
            Document doc = new Document(title, content);
            documentRepository.save(doc);
            logger.info("Saved document to database with ID: {}", doc.getId());

            // Split document into chunks
            dev.langchain4j.data.document.Document langchainDoc =
                dev.langchain4j.data.document.Document.from(content);

            DocumentSplitter splitter = DocumentSplitters.recursive(
                500, // chunk size
                50   // overlap
            );

            List<TextSegment> segments = splitter.split(langchainDoc);
            logger.info("Split document into {} segments", segments.size());

            // Generate embeddings and store
            for (TextSegment segment : segments) {
                Embedding embedding = embeddingModel.embed(segment).content();
                embeddingStore.add(embedding, segment);
            }

            logger.info("Successfully ingested document: {}", title);

        } catch (Exception e) {
            logger.error("Failed to ingest document: {}", title, e);
            throw new RuntimeException("Failed to ingest document: " + e.getMessage(), e);
        }
    }

    /**
     * Query the RAG system with a question
     */
    public String queryWithRag(String question) {
        try {
            logger.info("Processing RAG query: {}", question);

            if (chatModel == null) {
                return "Error: OpenAI API key not configured. Please set OPENAI_API_KEY environment variable.";
            }

            // Convert question to embedding
            Embedding questionEmbedding = embeddingModel.embed(question).content();

            // Find relevant document chunks
            List<EmbeddingMatch<TextSegment>> relevantMatches =
                embeddingStore.findRelevant(questionEmbedding, 3, 0.7);

            if (relevantMatches.isEmpty()) {
                logger.info("No relevant content found for query: {}", question);
                return "I couldn't find relevant information to answer your question. Please try rephrasing or upload more relevant documents.";
            }

            logger.info("Found {} relevant matches", relevantMatches.size());

            // Build context from relevant chunks
            String context = relevantMatches.stream()
                    .map(match -> match.embedded().text())
                    .collect(Collectors.joining("\n\n"));

            // Create prompt with context
            String prompt = String.format("""
                Based on the following context, please answer the question.
                If the answer is not clearly in the context, say so and suggest what additional information might be needed.

                Context:
                %s

                Question: %s

                Answer:
                """, context, question);

            // Generate answer using LLM
            String answer = chatModel.generate(prompt);
            logger.info("Generated answer for query: {}", question);

            return answer;

        } catch (Exception e) {
            logger.error("Error processing RAG query: {}", question, e);
            return "Error processing your question: " + e.getMessage();
        }
    }

    /**
     * Get all documents from the repository
     */
    public List<Document> getAllDocuments() {
        try {
            List<Document> documents = documentRepository.findAllByOrderByUploadedAtDesc();
            logger.info("Retrieved {} documents", documents.size());
            return documents;
        } catch (Exception e) {
            logger.error("Error retrieving documents", e);
            throw new RuntimeException("Failed to retrieve documents: " + e.getMessage(), e);
        }
    }

    /**
     * Search documents by title
     */
    public List<Document> searchDocumentsByTitle(String title) {
        try {
            return documentRepository.findByTitleContainingIgnoreCase(title);
        } catch (Exception e) {
            logger.error("Error searching documents by title: {}", title, e);
            throw new RuntimeException("Failed to search documents: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a document and its associated embeddings
     */
    public boolean deleteDocument(Long documentId) {
        try {
            if (documentRepository.existsById(documentId)) {
                documentRepository.deleteById(documentId);
                logger.info("Deleted document with ID: {}", documentId);
                // Note: This doesn't clean up embeddings from the vector store
                // In a production system, you'd want to track which embeddings belong to which document
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error deleting document with ID: {}", documentId, e);
            throw new RuntimeException("Failed to delete document: " + e.getMessage(), e);
        }
    }

    /**
     * Get document statistics
     */
    public String getDocumentStats() {
        try {
            long totalDocs = documentRepository.countAllDocuments();
            return String.format("Total documents in system: %d", totalDocs);
        } catch (Exception e) {
            logger.error("Error retrieving document statistics", e);
            return "Error retrieving statistics: " + e.getMessage();
        }
    }
}
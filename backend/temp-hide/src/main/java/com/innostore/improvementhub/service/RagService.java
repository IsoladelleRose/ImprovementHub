package com.innostore.improvementhub.service;

import com.innostore.improvementhub.entity.Document;
import com.innostore.improvementhub.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
public class RagService {

    private static final Logger logger = LoggerFactory.getLogger(RagService.class);
    private final DocumentRepository documentRepository;
    private final OpenAIService openAIService;

    @Autowired
    public RagService(DocumentRepository documentRepository, OpenAIService openAIService) {
        this.documentRepository = documentRepository;
        this.openAIService = openAIService;
        logger.info("Initialized RAG service with OpenAI integration");
    }

    /**
     * Simple document ingestion - just saves to database
     */
    public void ingestDocument(String title, String content) {
        try {
            logger.info("Starting document ingestion for: {}", title);
            Document doc = new Document(title, content);
            documentRepository.save(doc);
            logger.info("Saved document to database with ID: {}", doc.getId());
        } catch (Exception e) {
            logger.error("Failed to ingest document: {}", title, e);
            throw new RuntimeException("Failed to ingest document: " + e.getMessage(), e);
        }
    }

    /**
     * Query with RAG and OpenAI integration
     */
    public String queryWithRag(String question) {
        try {
            logger.info("Processing RAG query: {}", question);

            // Get relevant documents from the database
            List<Document> relevantDocs = searchRelevantDocuments(question);

            // Build context from documents
            StringBuilder context = new StringBuilder();
            if (!relevantDocs.isEmpty()) {
                context.append("Relevant context from your knowledge base:\n");
                for (Document doc : relevantDocs) {
                    context.append("- ").append(doc.getTitle()).append(": ")
                           .append(doc.getContent().substring(0, Math.min(200, doc.getContent().length())))
                           .append("...\n");
                }
                context.append("\n");
            }

            // Enhance the question with context
            String enhancedQuestion = context.toString() +
                "Based on the above context and your general knowledge, please answer this question: " + question;

            // Call OpenAI with the enhanced question
            return openAIService.queryOpenAI(enhancedQuestion);

        } catch (Exception e) {
            logger.error("Error processing RAG query: {}", question, e);
            return "Sorry, I encountered an error while processing your question: " + e.getMessage();
        }
    }

    /**
     * Search for relevant documents based on the question
     */
    private List<Document> searchRelevantDocuments(String question) {
        try {
            // Simple implementation: get all documents and return first few
            // In a real RAG system, this would use semantic search
            List<Document> allDocs = (List<Document>) documentRepository.findAll();
            return allDocs.size() > 3 ? allDocs.subList(0, 3) : allDocs;
        } catch (Exception e) {
            logger.error("Error searching relevant documents", e);
            return new ArrayList<>();
        }
    }

    /**
     * Get all documents from the repository
     */
    public List<Document> getAllDocuments() {
        try {
            List<Document> documents = (List<Document>) documentRepository.findAll();
            logger.info("Retrieved {} documents", documents.size());
            return documents;
        } catch (Exception e) {
            logger.error("Error retrieving documents", e);
            return new ArrayList<>();
        }
    }

    /**
     * Search documents by title
     */
    public List<Document> searchDocumentsByTitle(String title) {
        try {
            return getAllDocuments();
        } catch (Exception e) {
            logger.error("Error searching documents by title: {}", title, e);
            return new ArrayList<>();
        }
    }

    /**
     * Delete a document
     */
    public boolean deleteDocument(Long documentId) {
        try {
            if (documentRepository.existsById(documentId)) {
                documentRepository.deleteById(documentId);
                logger.info("Deleted document with ID: {}", documentId);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error deleting document with ID: {}", documentId, e);
            return false;
        }
    }

    /**
     * Get document statistics
     */
    public String getDocumentStats() {
        try {
            long totalDocs = documentRepository.count();
            return String.format("Total documents in system: %d", totalDocs);
        } catch (Exception e) {
            logger.error("Error retrieving document statistics", e);
            return "Error retrieving statistics: " + e.getMessage();
        }
    }

    /**
     * Test OpenAI connection with a simple query
     */
    public String testOpenAIConnection() {
        try {
            logger.info("Testing OpenAI connection");
            String testQuestion = "What is 2+2? Please respond with just the number.";
            String response = openAIService.queryOpenAI(testQuestion);
            logger.info("OpenAI test response received: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("OpenAI connection test failed", e);
            throw new RuntimeException("OpenAI test failed: " + e.getMessage(), e);
        }
    }
}
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
     * Analyze an idea and determine if it's a good idea
     */
    public String analyzeIdea(String coreConcept, String problemOpportunity) {
        try {
            logger.info("Analyzing idea: {}", coreConcept);

            // Build the analysis prompt
            String analysisPrompt = String.format(
                "Please analyze the following business idea:\n\n" +
                "Core Concept: %s\n\n" +
                "Problem/Opportunity: %s\n\n" +
                "Based on this information, provide a comprehensive analysis covering:\n" +
                "1. Is this a good idea? (Yes/No with brief reasoning)\n" +
                "2. Key strengths of the idea\n" +
                "3. Potential challenges or risks\n" +
                "4. How the idea can be executed (provide a practical implementation strategy or roadmap)\n" +
                "5. What profiles/roles are needed to realize this idea (describe the team composition and key skills required)\n" +
                "6. Initial recommendation\n\n" +
                "Keep the response well-structured, concise, and actionable. Use clear section headers for each point.",
                coreConcept,
                problemOpportunity
            );

            // Call OpenAI for analysis
            String analysis = openAIService.queryOpenAI(analysisPrompt);
            logger.info("Successfully analyzed idea: {}", coreConcept);

            return analysis;

        } catch (Exception e) {
            logger.error("Error analyzing idea: {}", coreConcept, e);
            return "Sorry, I encountered an error while analyzing the idea: " + e.getMessage();
        }
    }
}
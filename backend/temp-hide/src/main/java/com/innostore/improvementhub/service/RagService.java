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

    @Autowired
    public RagService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
        logger.info("Initialized simplified RAG service");
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
     * Simple mock query response
     */
    public String queryWithRag(String question) {
        try {
            logger.info("Processing RAG query: {}", question);

            // For now, return a simple mock response
            return String.format("Mock RAG Response: I received your question '%s'. " +
                "This is a simplified version of the RAG system. " +
                "The full AI-powered responses will be available once the complete system is deployed.",
                question);

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
            // Simple implementation - for now just return all documents
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
}
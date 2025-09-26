package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.entity.Document;
import com.innostore.improvementhub.service.RagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
@CrossOrigin(origins = {"http://localhost:4200", "https://improvementhub-frontend-production.up.railway.app"})
public class RagController {

    private static final Logger logger = LoggerFactory.getLogger(RagController.class);

    @Autowired
    private RagService ragService;

    /**
     * Upload a text file
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) {

        try {
            logger.info("Uploading file: {} with title: {}", file.getOriginalFilename(), title);

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Check file size (limit to 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("File size exceeds 10MB limit");
            }

            // Read file content as UTF-8 text
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);

            if (content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("File content is empty");
            }

            ragService.ingestDocument(title, content);
            logger.info("Successfully uploaded and processed file: {}", file.getOriginalFilename());

            return ResponseEntity.ok("Document uploaded successfully: " + title);

        } catch (Exception e) {
            logger.error("Failed to upload document: {}", title, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    /**
     * Upload text content directly
     */
    @PostMapping("/upload-text")
    public ResponseEntity<String> uploadText(@RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String content = request.get("content");

            logger.info("Uploading text content with title: {}", title);

            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Title is required");
            }

            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content is required");
            }

            // Check content length (limit to 100KB)
            if (content.length() > 100 * 1024) {
                return ResponseEntity.badRequest().body("Content exceeds 100KB limit");
            }

            ragService.ingestDocument(title.trim(), content.trim());
            logger.info("Successfully uploaded text content: {}", title);

            return ResponseEntity.ok("Text uploaded successfully: " + title);

        } catch (Exception e) {
            logger.error("Failed to upload text content", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    /**
     * Query the RAG system
     */
    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");

            if (question == null || question.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Question is required");
            }

            logger.info("Processing RAG query: {}", question);

            String answer = ragService.queryWithRag(question.trim());
            return ResponseEntity.ok(answer);

        } catch (Exception e) {
            logger.error("Failed to process query", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Query failed: " + e.getMessage());
        }
    }

    /**
     * Get all documents
     */
    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getDocuments() {
        try {
            List<Document> documents = ragService.getAllDocuments();
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Failed to retrieve documents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Search documents by title
     */
    @GetMapping("/documents/search")
    public ResponseEntity<List<Document>> searchDocuments(@RequestParam("title") String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            List<Document> documents = ragService.searchDocumentsByTitle(title.trim());
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Failed to search documents by title: {}", title, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Delete a document
     */
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable("id") Long documentId) {
        try {
            boolean deleted = ragService.deleteDocument(documentId);
            if (deleted) {
                return ResponseEntity.ok("Document deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Failed to delete document with ID: {}", documentId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed: " + e.getMessage());
        }
    }

    /**
     * Get document statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<String> getStats() {
        try {
            String stats = ragService.getDocumentStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Failed to retrieve document statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Stats retrieval failed: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("RAG system is running");
    }
}
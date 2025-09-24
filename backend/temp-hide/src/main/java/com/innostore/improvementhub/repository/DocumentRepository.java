package com.innostore.improvementhub.repository;

import com.innostore.improvementhub.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Find documents by title containing keyword (case insensitive)
    List<Document> findByTitleContainingIgnoreCase(String title);

    // Find documents by content containing keyword (case insensitive)
    @Query("SELECT d FROM Document d WHERE LOWER(d.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Document> findByContentContaining(@Param("keyword") String keyword);

    // Find documents uploaded after specific date
    List<Document> findByUploadedAtAfter(LocalDateTime date);

    // Find documents by content type
    List<Document> findByContentType(String contentType);

    // Find documents ordered by upload date (newest first)
    List<Document> findAllByOrderByUploadedAtDesc();

    // Count total documents
    @Query("SELECT COUNT(d) FROM Document d")
    long countAllDocuments();
}
package com.innostore.improvementhub.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "idea_analysis")
public class IdeaAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idea_id", nullable = false)
    private Long ideaId;

    @Column(name = "analysis", columnDefinition = "TEXT")
    private String analysis;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Default constructor
    public IdeaAnalysis() {}

    // Constructor with required fields
    public IdeaAnalysis(Long ideaId, String analysis) {
        this.ideaId = ideaId;
        this.analysis = analysis;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(Long ideaId) {
        this.ideaId = ideaId;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

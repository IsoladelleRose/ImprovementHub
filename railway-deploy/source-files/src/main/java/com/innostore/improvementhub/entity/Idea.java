package com.innostore.improvementhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ideas")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Core concept is required")
    @Column(name = "core_concept", columnDefinition = "TEXT")
    private String coreConcept;

    @NotBlank(message = "Problem or opportunity description is required")
    @Column(name = "problem_opportunity", columnDefinition = "TEXT")
    private String problemOpportunity;

    @Column(name = "wants_help")
    private Boolean wantsHelp;

    @Column(name = "user_role", columnDefinition = "TEXT")
    private String userRole;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // User relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor
    public Idea() {}

    // Constructor with required fields
    public Idea(String coreConcept, String problemOpportunity, String email) {
        this.coreConcept = coreConcept;
        this.problemOpportunity = problemOpportunity;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoreConcept() {
        return coreConcept;
    }

    public void setCoreConcept(String coreConcept) {
        this.coreConcept = coreConcept;
    }

    public String getProblemOpportunity() {
        return problemOpportunity;
    }

    public void setProblemOpportunity(String problemOpportunity) {
        this.problemOpportunity = problemOpportunity;
    }

    public Boolean getWantsHelp() {
        return wantsHelp;
    }

    public void setWantsHelp(Boolean wantsHelp) {
        this.wantsHelp = wantsHelp;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
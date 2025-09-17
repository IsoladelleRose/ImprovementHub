package com.innostore.improvementhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class IdeaRegistrationRequest {

    @NotBlank(message = "Core concept is required")
    private String coreConcept;

    @NotBlank(message = "Problem or opportunity description is required")
    private String problemOpportunity;

    private Boolean wantsHelp;

    private String userRole;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    // Default constructor
    public IdeaRegistrationRequest() {}

    // Constructor
    public IdeaRegistrationRequest(String coreConcept, String problemOpportunity, String email) {
        this.coreConcept = coreConcept;
        this.problemOpportunity = problemOpportunity;
        this.email = email;
    }

    // Getters and Setters
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
}
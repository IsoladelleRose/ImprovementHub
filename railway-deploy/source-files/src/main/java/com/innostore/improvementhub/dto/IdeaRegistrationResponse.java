package com.innostore.improvementhub.dto;

public class IdeaRegistrationResponse {

    private String message;
    private boolean savedToDatabase;

    public IdeaRegistrationResponse() {}

    public IdeaRegistrationResponse(String message, boolean savedToDatabase) {
        this.message = message;
        this.savedToDatabase = savedToDatabase;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSavedToDatabase() {
        return savedToDatabase;
    }

    public void setSavedToDatabase(boolean savedToDatabase) {
        this.savedToDatabase = savedToDatabase;
    }
}
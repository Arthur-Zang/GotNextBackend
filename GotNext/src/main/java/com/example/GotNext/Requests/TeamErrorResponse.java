package com.example.GotNext.Requests;

public class TeamErrorResponse {
    private String errorMessage;

    public TeamErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

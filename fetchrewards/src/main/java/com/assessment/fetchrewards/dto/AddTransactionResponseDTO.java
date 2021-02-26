package com.assessment.fetchrewards.dto;

import com.assessment.fetchrewards.model.Transaction;
import org.springframework.http.HttpStatus;

import java.util.List;

public class AddTransactionResponseDTO {
    String message;
    boolean success;
    String error;
    List<Transaction> response;
    HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Transaction> getResponse() {
        return response;
    }

    public void setResponse(List<Transaction> response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

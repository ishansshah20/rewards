package com.assessment.fetchrewards.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public class SpentPointsResponseDTO {

    String message;
    boolean success;
    String error;
    List response;
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

    public List getResponse() {
        return response;
    }

    public void setResponse(List response) {
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

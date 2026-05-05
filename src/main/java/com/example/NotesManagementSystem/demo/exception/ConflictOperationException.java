package com.example.NotesManagementSystem.demo.exception;

import org.springframework.http.HttpStatus;

public class ConflictOperationException extends ApiException {

    public ConflictOperationException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
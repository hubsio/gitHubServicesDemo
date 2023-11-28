package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends GitHubException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

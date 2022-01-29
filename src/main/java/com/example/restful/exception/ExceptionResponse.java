package com.example.restful.exception;

import lombok.Getter;

import java.util.Date;

@Getter
public class ExceptionResponse {
    private final Date timeStamp;
    private final String message;
    private final String details;

    public ExceptionResponse(Date timeStamp, String message, String details) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.details = details;
    }
}

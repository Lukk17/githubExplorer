package com.lukk.githubexplorer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.time.DateTimeException;

/**
 * A global exception handler that catches exceptions from all controllers.
 * Extends ResponseEntityExceptionHandler to handle standard Spring exceptions in a consistent manner.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all types of exceptions and returns a BAD REQUEST HTTP status.
     *
     * @param ex The exception that was thrown.
     * @return A response object that wraps the details of the error.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDefaultExceptions(Exception ex) {
        log.error("{} Exception class: {}", ex.getMessage(), ex.getClass().getSimpleName());

        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Server error, try again later.").build();
    }

    /**
     * Handles WebClientResponseException.UnprocessableEntity exceptions, usually indicating something is wrong with the API query.
     * Returns a BAD REQUEST HTTP status.
     *
     * @param ex The exception that was thrown.
     * @return A response object that wraps the details of the error.
     */
    @ExceptionHandler(WebClientResponseException.UnprocessableEntity.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleApiExceptions(WebClientResponseException.UnprocessableEntity ex) {
        log.error("{} Exception class: {}", ex.getMessage(), ex.getClass().getSimpleName());

        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST,
                        "Server error. There is possibly something wrong with your query")
                .build();
    }

    /**
     * Handles DateTimeException, indicating that the date passed in the request is not valid.
     * Returns a BAD REQUEST HTTP status.
     *
     * @param ex The exception that was thrown.
     * @return A response object that wraps the details of the error.
     */
    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateExceptions(DateTimeException ex) {
        log.error("{} Exception class: {}", ex.getMessage(), ex.getClass().getSimpleName());

        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST,
                        ex.getMessage())
                .build();
    }

    @ExceptionHandler(WebClientResponseException.Forbidden.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleForbiddenExceptions(WebClientResponseException.Forbidden ex) {
        log.error("{} Exception class: {}", ex.getMessage(), ex.getClass().getSimpleName());

        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST,
                        "To many requests. Use token to be able to be authorized and make more.")
                .build();
    }
}

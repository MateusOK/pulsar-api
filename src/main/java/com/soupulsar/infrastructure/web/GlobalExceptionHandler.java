package com.soupulsar.infrastructure.web;

import com.soupulsar.domain.exceptions.BusinessRuleException;
import com.soupulsar.domain.exceptions.NotFoundException;
import com.soupulsar.infrastructure.exceptions.EmailDeliveryException;
import com.soupulsar.infrastructure.exceptions.FailedAsaasCustomerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponse("404", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBusinessRuleException(BusinessRuleException e) {
        log.error(e.getMessage());
        return new ErrorResponse("409", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse("500", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ErrorResponse("400", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage());
        return new ErrorResponse("500", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(EmailDeliveryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleEmailDeliveryException(EmailDeliveryException e) {
        log.error(e.getMessage());
        return new ErrorResponse("500", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(FailedAsaasCustomerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFailedAsaasCustomerException(FailedAsaasCustomerException e) {
        log.error(e.getMessage());
        return new ErrorResponse("500", e.getMessage(), Instant.now());
    }
}
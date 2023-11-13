package com.SuperBank.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(DuplicateCompanyExceptiion.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleExceptionCompanyDuplicate(DuplicateCompanyExceptiion exception){
        return ResponseEntity.badRequest().body((exception.getMessage()));
    }
}

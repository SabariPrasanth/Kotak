package com.kotakNeo.kotakNeo.Exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex){
        return  (ex.getMessage() != null) ? ex.getMessage() : ex.getClass().getSimpleName();
    }
}

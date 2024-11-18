package com.example.wonjang.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleGlobalException(CustomException e, HttpServletRequest req, HttpServletResponse res) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(e).getBody();
        log.error("CustomException={}",e.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}

package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.response.ResponseFailed;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.security.SignatureException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseFailed> constrainViolation(ConstraintViolationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseFailed.builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseFailed> apiException(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(ResponseFailed.builder().error(exception.getReason()).build());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ResponseFailed> primaryException(SQLIntegrityConstraintViolationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseFailed.builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Token has expired");
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed JWT Token");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseFailed> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseFailed.builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseFailed> maxUploadSizeExceededException(MaxUploadSizeExceededException exception){
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ResponseFailed.builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ResponseFailed> tokensDontMatch(SignatureException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseFailed.builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseFailed> handleGlobalException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseFailed.builder().error(ex.getMessage()).build());
    }

}

package com.anikettcodes.healthjournal.exception;

import com.anikettcodes.healthjournal.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userAlreadyExists(UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User already exists!");
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> invalidTokenException(InvalidTokenException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
    }

}

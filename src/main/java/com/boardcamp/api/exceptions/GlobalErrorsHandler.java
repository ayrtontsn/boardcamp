package com.boardcamp.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorsHandler {
    
    @ExceptionHandler(GameNameConflict.class)
    public ResponseEntity<String> handleGameNameConflict(GameNameConflict exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(IdNotFound.class)
    public ResponseEntity<String> handleIdNotFound(IdNotFound exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(NoGameStock.class)
    public ResponseEntity<String> handleNoGameStock(NoGameStock exception){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

    @ExceptionHandler(GameReturned.class)
    public ResponseEntity<String> handleGameReturned(GameReturned exception){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

    @ExceptionHandler(NoGameReturned.class)
    public ResponseEntity<String> handleNoGameReturned(NoGameReturned exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
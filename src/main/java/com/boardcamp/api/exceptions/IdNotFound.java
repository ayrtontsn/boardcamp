package com.boardcamp.api.exceptions;

public class IdNotFound extends RuntimeException{
    public IdNotFound(String message){
        super(message);
    }
}

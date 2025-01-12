package com.boardcamp.api.exceptions;

public class NoGameReturned extends RuntimeException{
    public NoGameReturned(String message){
        super(message);
    }
}

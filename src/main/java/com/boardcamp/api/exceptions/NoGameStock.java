package com.boardcamp.api.exceptions;

public class NoGameStock extends RuntimeException{
    public NoGameStock(String message){
        super(message);
    }
}

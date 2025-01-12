package com.boardcamp.api.exceptions;

public class GameReturned extends RuntimeException{
    public GameReturned(String message){
        super(message);
    }
}

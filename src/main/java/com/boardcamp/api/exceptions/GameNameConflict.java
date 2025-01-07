package com.boardcamp.api.exceptions;

public class GameNameConflict extends RuntimeException{
    public GameNameConflict(String message){
        super(message);
    }    
}

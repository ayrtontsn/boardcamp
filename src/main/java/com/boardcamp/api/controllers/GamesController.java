package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.services.GamesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/games")
public class GamesController {

    final GamesService gamesService;

    GamesController(GamesService gamesService){
        this.gamesService = gamesService;
    }

    @GetMapping
    public ResponseEntity<Object> getGames(){
        return ResponseEntity.status(HttpStatus.OK).body(gamesService.getAllGames());
    }    
}

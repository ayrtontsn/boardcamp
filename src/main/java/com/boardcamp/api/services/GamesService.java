package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GamesRepository;

@Service
public class GamesService {
    
    final GamesRepository gamesRepository;

    GamesService(GamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }

    public List<GamesModel> getAllGames(){
        return gamesRepository.findAll();
    }
}

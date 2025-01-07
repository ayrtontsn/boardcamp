package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.GameNameConflict;
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

    public GamesModel postGame(GamesDTO dto){

        if(gamesRepository.existsGameByName(dto.getName())){
            throw new GameNameConflict("Game already registered");
        }

        GamesModel game = new GamesModel(dto);
        return gamesRepository.save(game);
    }
}

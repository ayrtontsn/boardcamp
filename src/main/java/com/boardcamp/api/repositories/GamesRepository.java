package com.boardcamp.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardcamp.api.models.GamesModel;

public interface GamesRepository extends JpaRepository<GamesModel, Long>{
    
}
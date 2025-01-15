package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GamesIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RentalsRepository rentalsRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private GamesRepository gamesRepository;

    @BeforeEach
    public void cleanUpDataBase(){
        rentalsRepository.deleteAll();
        customersRepository.deleteAll();
        gamesRepository.deleteAll();  
    }

    //HappyPath
    @Test
    void givenGameRepositoryEmpty_whenCreatingGame_thenCreateGame(){
        //given
        GamesDTO game = new GamesDTO("Test01","",10,10.0);


        HttpEntity<GamesDTO> body = new HttpEntity<>(game);
        //when
        ResponseEntity<GamesDTO> response = restTemplate.exchange(
            "/games",
            HttpMethod.POST,
            body,
            GamesDTO.class);
        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, gamesRepository.count());
    }

    @Test
    void givenRepeatedGame_whenCreatingGame_thenThrowsError(){
        //given
        GamesDTO game = new GamesDTO("Test02","",5,10.0);
        GamesModel gameRepeated = new GamesModel(game);
        
        gamesRepository.save(gameRepeated);

        HttpEntity<GamesDTO> body = new HttpEntity<>(game);
        
        //when
        ResponseEntity<String> response = restTemplate.exchange(
            "/games",
            HttpMethod.POST,
            body,
            String.class);

        // then

        assertEquals(1, gamesRepository.count());
        assertEquals("Game already registered", response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void givenGameInRepository_whenGetGame_thenReturnGames(){
        //given
        GamesDTO game = new GamesDTO("Test03","",5,10.0);

        GamesModel gameCreated = new GamesModel(game);
        
        gamesRepository.save(gameCreated);
        
        //when
        ResponseEntity<List> response = restTemplate.exchange(
            "/games",
            HttpMethod.GET,
            null,
            List.class);

        // then

        assertEquals(1, gamesRepository.count());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}

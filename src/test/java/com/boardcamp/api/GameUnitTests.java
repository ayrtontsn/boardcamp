package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.GameNameConflict;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.services.GamesService;

@SpringBootTest
public class GameUnitTests {
    @InjectMocks
    private GamesService gamesService;

    @Mock
    private GamesRepository gamesRepository;

    @Test
    void givenRepeatedNameGame_whenCreatingGame_thenThrowsError(){
        //given
        GamesDTO game = new GamesDTO("Test", "Test", 5, 5.0);
        doReturn(true).when(gamesRepository).existsGameByName(any());

        //when
        GameNameConflict exceptions = assertThrows(
            GameNameConflict.class, () -> gamesService.postGame(game));

        //then
        verify(gamesRepository,times(1)).existsGameByName(any());
        verify(gamesRepository,times(0)).save(any());
        assertNotNull(exceptions);
        assertEquals("Game already registered", exceptions.getMessage());
    }

    @Test
    void givenValidGame_whenCreatingGame_thenCreateGame(){
        //given
        GamesDTO game = new GamesDTO("Test", "Test", 5, 5.0);
        GamesModel newGame = new GamesModel(game);
        doReturn(false).when(gamesRepository).existsGameByName(any());
        doReturn(newGame).when(gamesRepository).save(any());

        //when
        GamesModel result = gamesService.postGame(game);

        //then
        verify(gamesRepository,times(1)).existsGameByName(any());
        verify(gamesRepository,times(1)).save(any());
        assertEquals(newGame,result);
    }
}

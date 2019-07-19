package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.exception.GameNotFoundException;
import com.company.chess_online_bakend_api.util.GameUtil;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoveServiceJpaImplTest {

    @Mock
    GameRepository gameRepository;

    @InjectMocks
    MoveServiceJpaImpl moveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPossibleMoves() throws MoveGeneratorException {
        Game game = GameUtil.initNewGame();
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        MoveCommand moveCommand1 = MoveCommand.builder().from("B1").to("C3").build();
        MoveCommand moveCommand2 = MoveCommand.builder().from("B1").to("A3").build();

        Set<MoveCommand> moveCommands = moveService.getPossibleMoves("B1", 1L);

        assertEquals(2, moveCommands.size());
        assertTrue(moveCommands.contains(moveCommand1));
        assertTrue(moveCommands.contains(moveCommand2));

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void getPossibleMovesNullFrom() throws MoveGeneratorException {
        Game game = GameUtil.initNewGame();
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        MoveCommand moveCommand1 = MoveCommand.builder().from("B1").to("C3").build();
        MoveCommand moveCommand2 = MoveCommand.builder().from("B1").to("A3").build();

        Set<MoveCommand> moveCommands = moveService.getPossibleMoves(null, 1L);

        assertEquals(20, moveCommands.size());
        assertTrue(moveCommands.contains(moveCommand1));
        assertTrue(moveCommands.contains(moveCommand2));

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void getPossibleMovesInvalidGameID() throws MoveGeneratorException {

        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> moveService.getPossibleMoves(null, 1L));
        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);
    }
}
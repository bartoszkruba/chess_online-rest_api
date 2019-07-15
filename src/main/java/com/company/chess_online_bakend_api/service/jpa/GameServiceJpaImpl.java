package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.converter.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.converter.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class GameServiceJpaImpl implements GameService {

    private final GameRepository gameRepository;
    private final RoomRepository roomRepository;

    private final GameToGameCommand gameToGameCommand;
    private final GameCommandToGame gameCommandToGame;

    public GameServiceJpaImpl(GameRepository gameRepository, RoomRepository roomRepository,
                              GameToGameCommand gameToGameCommand, GameCommandToGame gameCommandToGame) {
        this.gameRepository = gameRepository;
        this.roomRepository = roomRepository;
        this.gameToGameCommand = gameToGameCommand;
        this.gameCommandToGame = gameCommandToGame;
    }

    @Override
    public GameCommand getByRoomId(Long id) {
        return null;
    }

    @Override
    public GameCommand findById(Long aLong) {
        return null;
    }

    @Override
    public GameCommand save(GameCommand object) {
        return null;
    }

    @Override
    public Set<GameCommand> findAll() {
        return null;
    }

    @Override
    public void delete(GameCommand object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}

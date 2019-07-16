package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.converter.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.converter.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.exception.GameNotFoundException;
import com.company.chess_online_bakend_api.exception.RoomNotFoundException;
import com.company.chess_online_bakend_api.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
        return gameRepository
                .findGameByRoom(Room.builder().id(id).build())
                .map(gameToGameCommand::convert)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + id + "does not exist"));
    }

    @Override
    public GameCommand findById(Long id) {
        return gameRepository
                .findById(id)
                .map(gameToGameCommand::convert)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + id + "does not exist"));
    }

    @Override
    public GameCommand save(GameCommand gameCommand) {
        return gameToGameCommand.convert(gameRepository.save(gameCommandToGame.convert(gameCommand)));
    }

    @Override
    public Set<GameCommand> findAll() {
        Set<GameCommand> gameCommands = new HashSet<>();
        gameRepository
                .findAll()
                .forEach(game -> {
                    gameCommands.add(gameToGameCommand.convert(game));
                });
        return gameCommands;
    }

    @Override
    public void delete(GameCommand gameCommand) {
        gameRepository.delete(gameCommandToGame.convert(gameCommand));
    }

    @Override
    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }
}

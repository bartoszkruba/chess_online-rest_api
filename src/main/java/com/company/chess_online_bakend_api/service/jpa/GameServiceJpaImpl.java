/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.converter.command.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.converter.command.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.*;
import com.company.chess_online_bakend_api.service.GameService;
import com.company.chess_online_bakend_api.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class GameServiceJpaImpl implements GameService {

    private final SocketService socketService;

    private final GameRepository gameRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final GameToGameCommand gameToGameCommand;
    private final GameCommandToGame gameCommandToGame;

    @Autowired
    public GameServiceJpaImpl(SocketService socketService, GameRepository gameRepository, RoomRepository roomRepository,
                              UserRepository userRepository, GameToGameCommand gameToGameCommand,
                              GameCommandToGame gameCommandToGame) {
        this.socketService = socketService;
        this.gameRepository = gameRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.gameToGameCommand = gameToGameCommand;
        this.gameCommandToGame = gameCommandToGame;
    }

    @Override
    public GameCommand getByRoomId(Long id) {
        log.debug("Getting room by id " + id);

        return gameRepository
                .findGameByRoom(Room.builder().id(id).build())
                .map(gameToGameCommand::convert)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + id + "does not exist"));
    }

    @Override
    public GameCommand joinGame(PieceColor color, String username, Long gameId) {
        log.debug(username + " joining game with id " + gameId + ", place " + color);

        var user = userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist"));

        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " does not exist"));

        if (game.getStatus() != GameStatus.WAITNG_TO_START) {
            throw new GameAlreadyStartedException("Game with id " + gameId + " already started");
        }

        User player;
        User oppositePlayer;

        if (color == PieceColor.BLACK) {
            player = game.getBlackPlayer();
            oppositePlayer = game.getWhitePlayer();
        } else {
            player = game.getWhitePlayer();
            oppositePlayer = game.getBlackPlayer();
        }

        if (player != null) {
            throw new PlaceAlreadyTakenException("Place " + color + " in game with id " + gameId + " is already taken");
        }
        if (oppositePlayer != null &&
                oppositePlayer.getId() != null && oppositePlayer.getId().equals(user.getId())) {
            throw new AlreadyJoinedException("You already joined game with id " + gameId);
        }

        if (color == PieceColor.WHITE) {
            game.setWhitePlayer(user);
        } else {
            game.setBlackPlayer(user);
        }

        // TODO: 2019-07-27 socket broadcast

        return gameToGameCommand.convert(gameRepository.save(game));
    }

    @Override
    public GameCommand leaveGame(String username, Long gameId) {
        log.debug(username + " leaving game with id " + gameId);

        var user = userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist"));

        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " does not exist"));

        if (game.getWhitePlayer() != null && game.getWhitePlayer().getId() != null &&
                game.getWhitePlayer().getId().equals(user.getId())) {
            game.setWhitePlayer(null);
        } else if (game.getBlackPlayer() != null && game.getBlackPlayer().getId() != null &&
                game.getBlackPlayer().getId().equals(user.getId())) {
            game.setBlackPlayer(null);
        } else {
            throw new UserNotFoundException("You have not joined game with id " + gameId);
        }

        if (game.getStatus() != GameStatus.WAITNG_TO_START) {
            game.setStatus(GameStatus.STOPPED);
        }

        socketService.broadcastLeaveGame(game, user, game.getRoom().getId());

        return gameToGameCommand.convert(gameRepository.save(game));
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

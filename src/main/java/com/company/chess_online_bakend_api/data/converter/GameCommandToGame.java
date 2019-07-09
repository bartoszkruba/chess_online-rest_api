package com.company.chess_online_bakend_api.data.converter;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameCommandToGame implements Converter<GameCommand, Game> {

    private final UserCommandToUser userCommandToUser;

    @Autowired
    public GameCommandToGame(UserCommandToUser userCommandToUser) {
        this.userCommandToUser = userCommandToUser;
    }

    @Override
    @Nullable
    public Game convert(GameCommand gameCommand) {

        log.debug("Converting GameCommand to Game");

        if (gameCommand == null) {
            return null;
        }

        Game game = Game.builder()
                .id(gameCommand.getId())
                .turn(gameCommand.getTurn())
                .status(gameCommand.getStatus()).build();

        if (gameCommand.getWhitePlayer() != null) {
            game.setWhitePlayer(userCommandToUser.convert(gameCommand.getWhitePlayer()));
        }

        if (gameCommand.getBlackPlayer() != null) {
            game.setBlackPlayer(userCommandToUser.convert(gameCommand.getBlackPlayer()));
        }

        return game;
    }
}
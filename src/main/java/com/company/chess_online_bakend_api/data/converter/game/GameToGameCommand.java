package com.company.chess_online_bakend_api.data.converter.game;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.converter.user.UserToUserCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameToGameCommand implements Converter<Game, GameCommand> {

    private final UserToUserCommand userToUserCommand;

    @Autowired
    public GameToGameCommand(UserToUserCommand userToUserCommand) {
        this.userToUserCommand = userToUserCommand;
    }

    @Override
    @Nullable
    public GameCommand convert(Game game) {

        log.debug("Converting Game to GameCommand");

        if (game == null) {
            return null;
        }

        GameCommand gameCommand = GameCommand.builder()
                .status(game.getStatus())
                .turn(game.getTurn())
                .id(game.getId()).build();

        if (game.getRoom() != null) {
            gameCommand.setRoomId(game.getRoom().getId());
        }

        if (game.getWhitePlayer() != null) {
            gameCommand.setWhitePlayer(userToUserCommand.convert(game.getWhitePlayer()));
        }

        if (game.getBlackPlayer() != null) {
            gameCommand.setBlackPlayer(userToUserCommand.convert(game.getBlackPlayer()));
        }

        return gameCommand;
    }
}

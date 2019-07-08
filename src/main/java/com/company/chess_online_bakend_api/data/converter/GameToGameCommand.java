package com.company.chess_online_bakend_api.data.converter;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

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

        if (game == null) {
            return null;
        }

        GameCommand gameCommand = GameCommand.builder()
                .whitePlayer(userToUserCommand.convert(game.getWhitePlayer()))
                .blackPlayer(userToUserCommand.convert(game.getBlackPlayer()))
                .status(game.getStatus())
                .turn(game.getTurn())
                .id(game.getId()).build();

        if (game.getRoom() != null) {
            gameCommand.setRoomId(game.getRoom().getId());
        }

        return gameCommand;
    }
}

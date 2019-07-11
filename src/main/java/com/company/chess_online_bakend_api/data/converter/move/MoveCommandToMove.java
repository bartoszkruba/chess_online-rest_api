package com.company.chess_online_bakend_api.data.converter.move;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.util.PositionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MoveCommandToMove implements Converter<Move, MoveCommand> {

    @Override
    @Nullable
    public MoveCommand convert(Move move) {

        log.debug("Converting Move to MoveCommand");

        if (move == null) {
            return null;
        }

        return MoveCommand.builder()
                .id(move.getId())
                .happenedOn(move.getCreated())
                .count(move.getMoveCount())
                .pieceColor(move.getPieceColor())
                .pieceType(move.getPieceType())
                .from(PositionUtil.getPositionString(move.getHorizontalStartPosition(),
                        move.getVerticalStartPosition()))
                .to(PositionUtil.getPositionString(move.getHorizontalEndPosition(),
                        move.getVerticalEndPosition()))
                .build();

    }
}

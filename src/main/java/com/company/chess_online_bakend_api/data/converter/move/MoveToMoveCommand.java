package com.company.chess_online_bakend_api.data.converter.move;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.util.PositionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
@Slf4j
public class MoveToMoveCommand implements Converter<Move, MoveCommand> {

    @Override
    @Nullable
    public MoveCommand convert(Move move) {

        log.debug("Converting Move to MoveCommand");

        if (move == null) {
            return null;
        }

        MoveCommand moveCommand = MoveCommand.builder()
                .id(move.getId())
                .count(move.getMoveCount())
                .pieceColor(move.getPieceColor())
                .pieceType(move.getPieceType())
                .from(PositionUtils.getPositionString(move.getHorizontalStartPosition(),
                        move.getVerticalStartPosition()))
                .to(PositionUtils.getPositionString(move.getHorizontalEndPosition(),
                        move.getVerticalEndPosition()))
                .isKingSideCastle(move.getIsKingSideCastle())
                .isQueenSideCastle(move.getIsQueenSideCastle())
                .isKingAttacked(move.getIsKingAttacked())
                .isCheckmate(move.getIsCheckmate())
                .isDraw(move.getIsDraw())
                .build();

        if (move.getCreated() != null) {
            ZoneId zoneId = ZoneId.systemDefault();
            moveCommand.setTimestamp(move.getCreated().atZone(zoneId).toInstant().toEpochMilli());
        }

        return moveCommand;
    }
}

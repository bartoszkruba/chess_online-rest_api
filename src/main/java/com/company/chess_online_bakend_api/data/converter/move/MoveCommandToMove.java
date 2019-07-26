/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.move;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.util.PositionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;

@Slf4j
@Component
public class MoveCommandToMove implements Converter<MoveCommand, Move> {

    @Override
    @Nullable
    public Move convert(MoveCommand moveCommand) {
        log.debug("Converting MoveCommand to Move");

        if (moveCommand == null) {
            return null;
        }

        Move move = Move.builder()
                .id(moveCommand.getId())
                .moveCount(moveCommand.getCount())
                .pieceType(moveCommand.getPieceType())
                .pieceColor(moveCommand.getPieceColor())
                .horizontalStartPosition(PositionUtils.getHorizontalPosition(moveCommand.getFrom()))
                .verticalStartPosition(PositionUtils.getVerticalPosition(moveCommand.getFrom()))
                .horizontalEndPosition(PositionUtils.getHorizontalPosition(moveCommand.getTo()))
                .verticalEndPosition(PositionUtils.getVerticalPosition(moveCommand.getTo()))
                .isKingSideCastle(moveCommand.getIsKingSideCastle())
                .isQueenSideCastle(moveCommand.getIsQueenSideCastle())
                .isKingAttacked(moveCommand.getIsKingAttacked())
                .isCheckmate(moveCommand.getIsCheckmate())
                .isDraw(moveCommand.getIsDraw())
                .build();

        if (moveCommand.getTimestamp() != null) {
            Instant instant = Instant.ofEpochMilli(moveCommand.getTimestamp());
            move.setCreated(instant.atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        return move;
    }
}

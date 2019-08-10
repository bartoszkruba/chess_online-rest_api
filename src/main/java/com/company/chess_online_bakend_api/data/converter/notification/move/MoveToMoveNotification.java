/*
 * 8/3/19, 3:11 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.notification.move;

import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.data.notification.MoveNotification;
import com.company.chess_online_bakend_api.util.PositionUtils;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Slf4j
@Component
public class MoveToMoveNotification implements Converter<Move, MoveNotification> {

    @Nullable
    @Override
    public MoveNotification convert(Move move) {
        log.debug("Converting Move to MoveNotification");

        if (move == null) {
            return null;
        }

        var moveNotification = MoveNotification.builder()
                .moveCount(move.getMoveCount())
                .color(move.getPieceColor())
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
            moveNotification.setTimestamp(move.getCreated().atZone(zoneId).toInstant().toEpochMilli());
        }

        return moveNotification;
    }
}

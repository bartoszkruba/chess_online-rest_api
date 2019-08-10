/*
 * 7/27/19 3:26 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.converter.command.piece;

import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.model.Piece;
import com.company.chess_online_bakend_api.util.PositionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PieceToPieceCommand implements Converter<Piece, PieceCommand> {

    @Override
    @Nullable
    public PieceCommand convert(Piece piece) {

        log.debug("Converting Piece to PieceCommand");

        if (piece == null) {
            return null;
        }

        return PieceCommand.builder()
                .id(piece.getId())
                .pieceColor(piece.getPieceColor())
                .pieceType(piece.getPieceType())
                .moves(piece.getMoves())
                .position(PositionUtils.getPositionString(piece.getHorizontalPosition(),
                        piece.getVerticalPosition()))
                .build();
    }
}

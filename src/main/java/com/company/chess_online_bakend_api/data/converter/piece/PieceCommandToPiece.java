package com.company.chess_online_bakend_api.data.converter.piece;

import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.model.Piece;
import com.company.chess_online_bakend_api.util.PositionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PieceCommandToPiece implements Converter<PieceCommand, Piece> {

    @Override
    @Nullable
    public Piece convert(PieceCommand pieceCommand) {

        log.debug("Converting PieceCommand to Piece");

        if (pieceCommand == null) {
            return null;
        }

        return Piece.builder()
                .id(pieceCommand.getId())
                .pieceColor(pieceCommand.getPieceColor())
                .pieceType(pieceCommand.getPieceType())
                .horizontalPosition(PositionUtil.getHorizontalPosition(pieceCommand.getPosition()))
                .verticalPosition(PositionUtil.getVerticalPosition(pieceCommand.getPosition()))
                .build();
    }
}

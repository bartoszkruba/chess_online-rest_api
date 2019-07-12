package com.company.chess_online_bakend_api.data.converter.board;

import com.company.chess_online_bakend_api.data.command.BoardCommand;
import com.company.chess_online_bakend_api.data.converter.piece.PieceCommandToPiece;
import com.company.chess_online_bakend_api.data.model.Board;
import com.company.chess_online_bakend_api.data.model.Piece;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class BoardCommandToBoard implements Converter<BoardCommand, Board> {

    private final PieceCommandToPiece pieceCommandToPiece;

    @Autowired
    public BoardCommandToBoard(PieceCommandToPiece pieceCommandToPiece) {
        this.pieceCommandToPiece = pieceCommandToPiece;
    }

    @Override
    @Nullable
    public Board convert(BoardCommand boardCommand) {

        log.debug("Converting Board to BoardCommand");

        if (boardCommand == null) {
            return null;
        }

        Board board = Board.builder()
                .id(boardCommand.getId()).build();

        if (boardCommand.getPieces() != null) {
            Set<Piece> pieces = new HashSet<>();
            boardCommand.getPieces().forEach(piece -> pieces.add(pieceCommandToPiece.convert(piece)));
            board.setPieces(pieces);
        } else {
            board.setPieces(null);
        }

        return board;
    }
}

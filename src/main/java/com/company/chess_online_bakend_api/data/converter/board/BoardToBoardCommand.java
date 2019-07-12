package com.company.chess_online_bakend_api.data.converter.board;

import com.company.chess_online_bakend_api.data.command.BoardCommand;
import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.converter.piece.PieceToPieceCommand;
import com.company.chess_online_bakend_api.data.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class BoardToBoardCommand implements Converter<Board, BoardCommand> {

    private final PieceToPieceCommand pieceToPieceCommand;

    @Autowired
    public BoardToBoardCommand(PieceToPieceCommand pieceToPieceCommand) {
        this.pieceToPieceCommand = pieceToPieceCommand;
    }

    @Override
    @Nullable
    public BoardCommand convert(Board board) {

        log.debug("Converting Board to BoardCommand");

        if (board == null) {
            return null;
        }

        BoardCommand boardCommand = BoardCommand.builder().build();
        boardCommand.setId(board.getId());

        if (board.getPieces() != null) {
            Set<PieceCommand> commandPieces = boardCommand.getPieces();
            board.getPieces().forEach(piece -> commandPieces.add(pieceToPieceCommand.convert(piece)));
        } else {
            boardCommand.setPieces(null);
        }

        return boardCommand;
    }
}

package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.Board;
import com.company.chess_online_bakend_api.data.model.Piece;
import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BoardUtil {

    // TODO: 2019-07-14 write tests
    public static Board initNewBoard() {
        log.debug("Initializing new board");

        Set<Piece> pieces = new HashSet<>();
        addPawns(pieces);
        addRooks(pieces);
        addKnights(pieces);
        addBishops(pieces);
        addKings(pieces);
        addQueens(pieces);

        return Board.builder().pieces(pieces).build();
    }

    private static void addPawns(Set<Piece> pieces) {
        for (HorizontalPosition pos :
                HorizontalPosition.values()) {

            Piece piece = Piece.builder()
                    .pieceColor(PieceColor.WHITE)
                    .pieceType(PieceType.PAWN)
                    .verticalPosition(VerticalPosition.TWO)
                    .horizontalPosition(pos).build();

            pieces.add(piece);
        }

        for (HorizontalPosition pos :
                HorizontalPosition.values()) {

            Piece piece = Piece.builder()
                    .pieceColor(PieceColor.BLACK)
                    .pieceType(PieceType.PAWN)
                    .verticalPosition(VerticalPosition.SEVEN)
                    .horizontalPosition(pos).build();

            pieces.add(piece);
        }
    }

    private static void addRooks(Set<Piece> pieces) {
        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.ROOK)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.A)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.ROOK)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.H)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.ROOK)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.A)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.ROOK)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.H)
                .build());
    }

    private static void addKnights(Set<Piece> pieces) {
        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.KNIGHT)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.B)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.KNIGHT)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.G)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.KNIGHT)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.B)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.KNIGHT)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.G)
                .build());
    }

    private static void addBishops(Set<Piece> pieces) {
        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.BISHOP)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.C)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.BISHOP)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.F)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.BISHOP)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.C)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.BISHOP)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.F)
                .build());
    }

    private static void addKings(Set<Piece> pieces) {
        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.KING)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.E)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.KING)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.E)
                .build());
    }

    private static void addQueens(Set<Piece> pieces) {
        pieces.add(Piece.builder().pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.QUEEN)
                .verticalPosition(VerticalPosition.ONE)
                .horizontalPosition(HorizontalPosition.D)
                .build());

        pieces.add(Piece.builder().pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.QUEEN)
                .verticalPosition(VerticalPosition.EIGHT)
                .horizontalPosition(HorizontalPosition.D)
                .build());
    }
}

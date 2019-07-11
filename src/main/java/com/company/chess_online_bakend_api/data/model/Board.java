package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Board extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Piece> pieces;

    @Builder
    public Board(Long id, LocalDateTime created, LocalDateTime updated) {
        super(id, created, updated);
        resetBoard();
    }

    // TODO: 2019-07-11 Move to service
    // TODO: 2019-07-11 write tests
    private void resetBoard() {
        log.debug("Reseting board");
        this.pieces = new HashSet<>();
        addPawns();
        addRooks();
        addKnights();
        addBishops();
        addKings();
        addQueens();
    }

    private void addPawns() {
        log.debug("Adding pawns");

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

    private void addRooks() {
        log.debug("Adding rooks");

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

    private void addKnights() {
        log.debug("Adding knights");

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

    private void addBishops() {
        log.debug("Adding bishops");

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

    private void addKings() {
        log.debug("Adding kings");

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

    private void addQueens() {
        log.debug("Adding Queens");
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

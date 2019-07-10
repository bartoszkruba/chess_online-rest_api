package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Piece extends BaseEntity {

    @Enumerated
    PieceColor pieceColor;

    @Enumerated
    PieceType pieceType;

    @Enumerated
    HorizontalPosition horizontalPosition;

    @Enumerated
    VerticalPosition verticalPosition;

    @Builder
    public Piece(Long id, LocalDateTime created, LocalDateTime updated, PieceColor pieceColor, PieceType pieceType,
                 HorizontalPosition horizontalPosition, VerticalPosition verticalPosition) {
        super(id, created, updated);
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.horizontalPosition = horizontalPosition;
        this.verticalPosition = verticalPosition;
    }
}

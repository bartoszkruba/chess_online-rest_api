package com.company.chess_online_bakend_api.data.model;

import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
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
public class Move extends BaseEntity {

    @Enumerated
    private HorizontalPosition horizontalStartPosition;

    @Enumerated
    private VerticalPosition verticalStartPosition;

    @Enumerated
    private HorizontalPosition horizontalEndPosition;

    @Enumerated
    private VerticalPosition verticalEndPosition;

    @Enumerated
    private PieceColor pieceColor;

    @Enumerated
    private PieceType pieceType;

    private Integer moveCount;

    @Builder
    public Move(Long id, LocalDateTime created, LocalDateTime updated, HorizontalPosition horizontalStartPosition,
                VerticalPosition verticalStartPosition, HorizontalPosition horizontalEndPosition,
                VerticalPosition verticalEndPosition, PieceColor pieceColor, PieceType pieceType, Integer moveCount) {

        super(id, created, updated);
        this.horizontalStartPosition = horizontalStartPosition;
        this.verticalStartPosition = verticalStartPosition;
        this.horizontalEndPosition = horizontalEndPosition;
        this.verticalEndPosition = verticalEndPosition;
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.moveCount = moveCount;
    }
}

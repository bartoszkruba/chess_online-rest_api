/*
 * 8/3/19, 3:04 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.notification;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoveNotification extends BaseNotification {

    private Long timestamp;

    private String from;
    private String to;
    private PieceColor color;
    private PieceType pieceType;
    private Boolean isKingSideCastle;
    private Boolean isQueenSideCastle;
    private Boolean isKingAttacked;
    private Boolean isCheckmate;
    private Boolean isDraw;
    private Integer moveCount;

    @Builder
    public MoveNotification(Long timestamp, String from, String to, PieceColor color,
                            PieceType pieceType, Boolean isKingSideCastle, Boolean isQueenSideCastle, Boolean isKingAttacked,
                            Boolean isCheckmate, Boolean isDraw, Integer moveCount) {

        super(NotificationType.MOVE);
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.color = color;
        this.pieceType = pieceType;
        this.isKingSideCastle = isKingSideCastle;
        this.isQueenSideCastle = isQueenSideCastle;
        this.isKingAttacked = isKingAttacked;
        this.isCheckmate = isCheckmate;
        this.isDraw = isDraw;
        this.moveCount = moveCount;
    }
}

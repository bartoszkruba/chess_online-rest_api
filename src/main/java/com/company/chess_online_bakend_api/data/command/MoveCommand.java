package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidPositionConstraint;
import com.company.chess_online_bakend_api.data.validation.group.OnCreateNewMove;
import com.company.chess_online_bakend_api.data.validation.group.OnGetPossibleMoves;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoveCommand extends BaseEntityCommand {

    public static final String MESSAGE_FROM_NULL = "Start position cannot be null";
    public static final String MESSAGE_TO_NULL = "New position cannot be null";

    @NotNull(groups = OnCreateNewMove.class, message = MESSAGE_FROM_NULL)
    @ValidPositionConstraint(groups = {OnCreateNewMove.class, OnGetPossibleMoves.class})
    private String from;

    @NotNull(groups = OnCreateNewMove.class, message = MESSAGE_TO_NULL)
    @ValidPositionConstraint(groups = OnCreateNewMove.class)
    private String to;
    private PieceColor pieceColor;
    private PieceType pieceType;

    private Boolean isKingSideCastle;
    private Boolean isQueenSideCastle;
    private Boolean isKingAttacked;
    private Boolean isCheckmate;
    private Boolean isDraw;
    private Integer count;

    // TODO: 2019-07-11 Consider switching to epoch time
    private LocalDateTime happenedOn;

    @Builder
    public MoveCommand(Long id, String from, String to, PieceColor pieceColor, PieceType pieceType, Integer count,
                       LocalDateTime happenedOn, Boolean isKingSideCastle, Boolean isQueenSideCastle,
                       Boolean isKingAttacked, Boolean isCheckmate, Boolean isDraw) {
        super(id);
        this.from = from;
        this.to = to;
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.count = count;
        this.happenedOn = happenedOn;
        this.isKingSideCastle = isKingSideCastle;
        this.isQueenSideCastle = isQueenSideCastle;
        this.isKingAttacked = isKingAttacked;
        this.isCheckmate = isCheckmate;
        this.isDraw = isDraw;
    }
}

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidPositionConstraint;
import com.company.chess_online_bakend_api.data.validation.group.OnCreateNewMove;
import com.company.chess_online_bakend_api.data.validation.group.OnGetPossibleMoves;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

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
    @ApiModelProperty("From which square figure moved")
    private String from;

    @NotNull(groups = OnCreateNewMove.class, message = MESSAGE_TO_NULL)
    @ValidPositionConstraint(groups = OnCreateNewMove.class)
    @ApiModelProperty("Figures new position")
    private String to;
    @ApiModelProperty("Piece color")
    private PieceColor pieceColor;
    @ApiModelProperty("Piece type")
    private PieceType pieceType;

    @ApiModelProperty("Is move a king side castle")
    private Boolean isKingSideCastle;
    @ApiModelProperty("Is move a queen side castle")
    private Boolean isQueenSideCastle;
    @ApiModelProperty("Will king be attacked after move")
    private Boolean isKingAttacked;
    @ApiModelProperty("Is it a checkmate move")
    private Boolean isCheckmate;
    @ApiModelProperty("Is it a draw move")
    private Boolean isDraw;
    @ApiModelProperty("Move count")
    private Integer count;

    @ApiModelProperty("Move timestamp")
    private Long timestamp;

    @Builder
    public MoveCommand(Long id, String from, String to, PieceColor pieceColor, PieceType pieceType, Integer count,
                       Long timestamp, Boolean isKingSideCastle, Boolean isQueenSideCastle,
                       Boolean isKingAttacked, Boolean isCheckmate, Boolean isDraw) {
        super(id);
        this.from = from;
        this.to = to;
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.count = count;
        this.timestamp = timestamp;
        this.isKingSideCastle = isKingSideCastle;
        this.isQueenSideCastle = isQueenSideCastle;
        this.isKingAttacked = isKingAttacked;
        this.isCheckmate = isCheckmate;
        this.isDraw = isDraw;
    }
}

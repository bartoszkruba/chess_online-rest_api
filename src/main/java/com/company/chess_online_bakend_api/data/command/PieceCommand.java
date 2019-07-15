package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.model.enums.PieceType;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidPositionConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PieceCommand extends BaseEntityCommand {

    Integer moves;

    PieceColor pieceColor;
    PieceType pieceType;

    @ValidPositionConstraint
    String position;

    @Builder
    public PieceCommand(Long id, PieceColor pieceColor, PieceType pieceType, String position, Integer moves) {
        super(id);
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.position = position;
        this.moves = moves;
    }

}

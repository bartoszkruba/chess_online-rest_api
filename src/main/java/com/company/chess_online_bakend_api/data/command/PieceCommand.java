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
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PieceCommand extends BaseEntityCommand {

    @ApiModelProperty("How many times did figure moved")
    Integer moves;

    @ApiModelProperty("Piece color")
    PieceColor pieceColor;
    @ApiModelProperty("Piece type")
    PieceType pieceType;

    @ApiModelProperty("Piece current position on board")
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

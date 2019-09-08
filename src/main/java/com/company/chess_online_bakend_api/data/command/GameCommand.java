/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameCommand extends BaseEntityCommand {

    @ApiModelProperty(value = "Games board")
    private BoardCommand board;
    @ApiModelProperty(value = "Room id")
    private Long roomId;
    @ApiModelProperty(value = "Game status")
    private GameStatus status;
    @ApiModelProperty("White player")
    private UserCommand whitePlayer;
    @ApiModelProperty("Black player")
    private UserCommand blackPlayer;
    @ApiModelProperty("Is king attacked right now")
    private Boolean isKingAttacked;
    @ApiModelProperty("Is game over with checkmate")
    private Boolean isCheckmate;
    @ApiModelProperty("Is game over with draw")
    private Boolean isDraw;
    @ApiModelProperty("Turn count")
    private Integer turn;
    @ApiModelProperty("Current fen notation for game")
    private String fenNotation;

    @Builder
    public GameCommand(Long id, Long roomId, GameStatus status, UserCommand whitePlayer, UserCommand blackPlayer,
                       Integer turn, BoardCommand boardCommand, String fenNotation,
                       Boolean isKingAttacked, Boolean isCheckmate, Boolean isDraw) {
        super(id);
        this.roomId = roomId;
        this.status = status;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.turn = turn;
        this.board = boardCommand;
        this.fenNotation = fenNotation;
        this.isKingAttacked = isKingAttacked;
        this.isCheckmate = isCheckmate;
        this.isDraw = isDraw;
    }
}

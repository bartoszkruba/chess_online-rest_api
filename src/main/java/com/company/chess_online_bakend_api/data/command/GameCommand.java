/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameCommand extends BaseEntityCommand {

    private BoardCommand board;
    private Long roomId;
    private GameStatus status;
    private UserCommand whitePlayer;
    private UserCommand blackPlayer;
    private Boolean isKingAttacked;
    private Boolean isCheckmate;
    private Boolean isDraw;
    private Integer turn;
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

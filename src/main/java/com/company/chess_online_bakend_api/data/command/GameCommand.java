package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.model.GameStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameCommand extends BaseEntityCommand {

    private Long roomId;
    private GameStatus status;
    private UserCommand whitePlayer;
    private UserCommand blackPlayer;
    private Integer turn;

    @Builder
    public GameCommand(Long id, Long roomId, GameStatus status, UserCommand whitePlayer, UserCommand blackPlayer,
                       Integer turn) {
        super(id);
        this.roomId = roomId;
        this.status = status;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.turn = turn;
    }
}

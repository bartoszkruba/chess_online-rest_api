/*
 * 8/3/19, 4:50 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.notification;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.notification.enums.GameOverCause;
import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameOverNotification extends BaseNotification {

    private String fenNotation;
    private GameOverCause gameOverCause;
    private UserNotification winner;
    private PieceColor winnerColor;
    private Long gameId;

    @Builder
    public GameOverNotification(GameOverCause gameOverCause, UserNotification winner,
                                PieceColor winnerColor, Long gameId, String fenNotation) {
        super(NotificationType.GAME_OVER);
        this.gameOverCause = gameOverCause;
        this.winner = winner;
        this.winnerColor = winnerColor;
        this.gameId = gameId;
        this.fenNotation = fenNotation;
    }
}
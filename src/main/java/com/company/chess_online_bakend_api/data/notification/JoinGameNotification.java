/*
 * 7/27/19 5:25 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.notification;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinGameNotification extends BaseNotification {
    private String fenNotation;
    private Long gameId;
    private UserNotification user;
    private PieceColor color;

    @Builder
    public JoinGameNotification(Long gameId, UserNotification user, PieceColor color, String fenNotation) {
        super(NotificationType.JOIN_GAME);
        this.gameId = gameId;
        this.user = user;
        this.color = color;
        this.fenNotation = fenNotation;
    }
}

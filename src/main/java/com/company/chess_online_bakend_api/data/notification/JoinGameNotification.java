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
@JsonInclude
public class JoinGameNotification extends BaseNotification {
    String fenNotation;
    Long gameId;
    UserNotification user;
    PieceColor color;

    public JoinGameNotification(NotificationType notificationType, Long gameId, UserNotification user, PieceColor color,
                                String fenNotation) {
        super(notificationType);
        this.gameId = gameId;
        this.user = user;
        this.color = color;
        this.fenNotation = fenNotation;
    }
}

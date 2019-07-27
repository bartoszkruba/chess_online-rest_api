/*
 * 7/27/19 5:39 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.notification;

import com.company.chess_online_bakend_api.data.model.enums.PieceColor;
import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@JsonInclude
public class LeaveGameNotification extends BaseNotification {
    String fenNotation;
    Long gameId;
    UserNotification user;
    PieceColor color;

    @Builder
    public LeaveGameNotification(String fenNotation, Long gameId, UserNotification user, PieceColor color) {
        super(NotificationType.LEAVE_GAME);
        this.fenNotation = fenNotation;
        this.gameId = gameId;
        this.user = user;
        this.color = color;
    }
}

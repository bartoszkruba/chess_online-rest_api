/*
 * 7/27/19 3:20 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.notification;

import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageNotification extends BaseNotification {
    private Long roomId;
    private String username;
    private Long userId;
    private String message;

    private Long timestamp;

    @Builder
    public ChatMessageNotification(NotificationType notificationType, Long roomId, String username, Long userId,
                                   Long timestamp, String message) {
        super(notificationType);
        this.roomId = roomId;
        this.username = username;
        this.userId = userId;
        this.timestamp = timestamp;
        this.message = message;
    }
}

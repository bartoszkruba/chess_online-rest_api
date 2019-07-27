/*
 * 7/27/19 3:22 PM. Updated by Bartosz Kruba.
 */

/*
 * 7/27/19 3:22 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.notification;

import com.company.chess_online_bakend_api.data.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseNotification {
    NotificationType notificationType;
}

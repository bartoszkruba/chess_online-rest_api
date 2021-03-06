/*
 * 7/27/19 5:26 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNotification {
    String username;
    Long id;
}

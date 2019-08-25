/*
 * 8/25/19, 8:59 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class WebSocketId {
    String connectionId;

    @ManyToOne
    User user;

    @Builder
    public WebSocketId(String connectionId, User user) {
        this.connectionId = connectionId;
        this.user = user;
    }
}

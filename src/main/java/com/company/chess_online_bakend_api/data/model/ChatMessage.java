package com.company.chess_online_bakend_api.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true, exclude = {"room", "user"})
@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class ChatMessage extends BaseEntity {

    @ManyToOne
    private Room room;

    @ManyToOne
    private User user;

    private String message;

    @Builder
    public ChatMessage(Long id, LocalDateTime created, LocalDateTime updated, Room room, User user, String message) {
        super(id, created, updated);
        this.room = room;
        this.user = user;
        this.message = message;
    }
}

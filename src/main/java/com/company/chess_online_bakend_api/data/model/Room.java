/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Room extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Game game;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessages;

    @Column(unique = true)
    private String name;

    @Builder
    public Room(Long id, LocalDateTime created, LocalDateTime updated, String name, Game game,
                List<ChatMessage> chatMessages) {
        super(id, created, updated);
        this.name = name;
        this.game = game;
        this.chatMessages = chatMessages;
    }

    public void addGame(Game game) {
        this.game = game;
        game.setRoom(this);
    }

    public void addChatMessage(ChatMessage chatMessage) {
        if (this.chatMessages == null) {
            this.chatMessages = new ArrayList<>();
        }

        this.chatMessages.add(chatMessage);

        chatMessage.setRoom(this);
    }
}

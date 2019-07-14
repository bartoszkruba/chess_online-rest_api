package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Room extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Game game;

    @Column(unique = true)
    private String name;

    @Builder
    public Room(Long id, LocalDateTime created, LocalDateTime updated, String name) {
        super(id, created, updated);
        this.name = name;
    }

    public void addGame(Game game) {
        this.game = game;
        game.setRoom(this);
    }
}

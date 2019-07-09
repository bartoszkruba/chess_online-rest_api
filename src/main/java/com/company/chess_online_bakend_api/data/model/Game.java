package com.company.chess_online_bakend_api.data.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true, exclude = "room")
@ToString(exclude = "room")
@Entity
@Data
@NoArgsConstructor
public class Game extends BaseEntity {

    @OneToOne
    private Room room;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @ManyToOne
    private User whitePlayer;

    @ManyToOne
    private User blackPlayer;

    private Integer turn;

    @Builder
    public Game(Long id, LocalDateTime created, LocalDateTime updated, Room room, GameStatus status, User whitePlayer,
                User blackPlayer, Integer turn) {
        super(id, created, updated);
        this.room = room;
        this.status = status;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.turn = turn;
    }
}

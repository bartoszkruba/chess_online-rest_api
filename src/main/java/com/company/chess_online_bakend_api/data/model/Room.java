package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Room extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private Game game;

    @Column(unique = true)
    private String name;

    @Builder
    public Room(Long id, LocalDateTime created, LocalDateTime updated, String name) {
        super(id, created, updated);
        this.name = name;

        startNewGame();
    }

    public Game startNewGame() {

        log.debug("Creating new empty game...");

        Game game = Game.builder()
                .status(GameStatus.WAITNG_TO_START)
                .turn(0)
                .room(this)
                .build();
        this.game = game;

        // TODO: 2019-07-08 Consider creating archive microservice / RabbitMQ
        // TODO: 2019-07-08 move old game to archive

        return game;
    }

    public Game startNewGame(@NotNull User whitePlayer, @NotNull User blackPlayer) {

        log.debug("Creating new game between: " + whitePlayer + " and " + blackPlayer);

        if (whitePlayer == null || blackPlayer == null) {
            log.error("One of players is null");
            throw new NullPointerException();
        }

        Game game = Game.builder().status(GameStatus.STARTED)
                .turn(1)
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .room(this).build();

        this.game = game;

        return game;
    }
}

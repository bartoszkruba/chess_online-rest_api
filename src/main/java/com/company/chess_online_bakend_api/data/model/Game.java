/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.model;

import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = "room")
@ToString(exclude = "room", callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Game extends BaseEntity {

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    private Room room;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Board board;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Move> moves;

    @ManyToOne
    private User whitePlayer;
    // Used to determine if player disconnected
    private LocalDateTime whitePing;

    @ManyToOne
    private User blackPlayer;
    // Used to determine if player disconnected
    private LocalDateTime blackPing;

    private Boolean isKingAttacked;
    private Boolean isCheckmate;
    private Boolean isDraw;
    private Integer turn;
    private String fenNotation;

    @Builder
    public Game(Long id, LocalDateTime created, LocalDateTime updated, Room room, GameStatus status, User whitePlayer,
                User blackPlayer, Integer turn, Board board, String fenNotation, List<Move> moves,
                Boolean isKingAttacked, Boolean isCheckmate, Boolean isDraw, LocalDateTime whitePing,
                LocalDateTime blackPing) {
        super(id, created, updated);
        this.room = room;
        this.status = status;
        this.whitePlayer = whitePlayer;
        this.whitePing = whitePing;
        this.blackPlayer = blackPlayer;
        this.blackPing = blackPing;
        this.turn = turn;
        this.board = board;
        this.fenNotation = fenNotation;
        this.moves = moves;
        this.isKingAttacked = isKingAttacked;
        this.isCheckmate = isCheckmate;
        this.isDraw = isDraw;
    }

    public void addMove(Move move) {

        if (move == null) {
            throw new RuntimeException("Null value passed");
        }

        if (this.moves == null) {
            moves = new ArrayList<>();
        }

        moves.add(move);
    }

    public void increaseTurnCount() {
        if (turn == null || turn == 0) {
            turn = 1;
        }
        turn++;
    }
}

package com.company.chess_online_bakend_api.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true, exclude = "room")
@Entity
@Data
@NoArgsConstructor
public class Game extends BaseEntity{

    @OneToOne
    Room room;

    GameStatus status;

    @ManyToOne
    User whitePlayer;

    @ManyToOne
    User blackPlayer;

    Integer turn;
}

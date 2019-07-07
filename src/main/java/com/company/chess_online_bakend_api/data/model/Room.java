package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Room extends BaseEntity {

    @OneToOne
    Game game;

    String name;

    @Builder
    public Room(Long id, LocalDateTime created, LocalDateTime updated, String name) {
        super(id, created, updated);
        this.name = name;
    }
}

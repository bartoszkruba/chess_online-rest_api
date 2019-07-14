package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Board extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Piece> pieces;

    @Builder
    public Board(Long id, LocalDateTime created, LocalDateTime updated, Set<Piece> pieces) {
        super(id, created, updated);
        this.pieces = pieces;
    }
}

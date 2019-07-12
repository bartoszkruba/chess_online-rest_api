package com.company.chess_online_bakend_api.data.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomCommand extends BaseEntityCommand {

    GameCommand game;

    String name;

    @Builder
    public RoomCommand(Long id, GameCommand game, String name) {
        super(id);
        this.game = game;
        this.name = name;
    }
}

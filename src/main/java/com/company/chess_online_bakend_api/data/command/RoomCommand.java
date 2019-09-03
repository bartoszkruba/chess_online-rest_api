/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.validation.constraint.UniqueRoomNameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomCommand extends BaseEntityCommand {

    public static final String NAME_NOT_EMPTY_MESSAGE = "Name cannot be empty";
    public static final String NAME_SIZE_MESSAGE = "Name must be between 3 and 40 characters long";

    private GameCommand game;

    @NotEmpty(message = NAME_NOT_EMPTY_MESSAGE)
    @Size(min = 3, max = 40, message = NAME_SIZE_MESSAGE)
    @UniqueRoomNameConstraint
    @ApiModelProperty(required = true)
    private String name;

    private GameStatus gameStatus;

    @Builder
    public RoomCommand(Long id, GameCommand game, String name, GameStatus gameStatus) {
        super(id);
        this.game = game;
        this.name = name;
        this.gameStatus = gameStatus;
    }
}

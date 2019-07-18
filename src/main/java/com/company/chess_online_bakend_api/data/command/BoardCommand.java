package com.company.chess_online_bakend_api.data.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardCommand extends BaseEntityCommand {

    Set<PieceCommand> pieces;

    @Builder
    public BoardCommand(Long id, Set<PieceCommand> pieces) {

        super(id);

        if (pieces == null) {
            this.pieces = new HashSet<>();
        } else {
            this.pieces = pieces;
        }
    }
}

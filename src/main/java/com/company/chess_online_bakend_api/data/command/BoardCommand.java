/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "List with all boards pieces")
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

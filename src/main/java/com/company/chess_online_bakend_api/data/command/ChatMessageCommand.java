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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageCommand extends BaseEntityCommand {

    public static final String MESSAGE_NOT_NULL_ERROR = "Message cannot be null";
    public static final String MESSAGE_LENGTH_ERROR = "Message needs to be between 1 and 500 characters long";

    private Long roomId;
    private String username;
    private Long userId;

    private Long timestamp;

    @NotBlank(message = MESSAGE_NOT_NULL_ERROR)
    @Size(max = 500, message = MESSAGE_LENGTH_ERROR)
    @ApiModelProperty(required = true)
    private String message;

    @Builder
    public ChatMessageCommand(Long id, Long roomId, String username, Long userId, Long timestamp, String message) {
        super(id);
        this.roomId = roomId;
        this.username = username;
        this.userId = userId;
        this.timestamp = timestamp;
        this.message = message;
    }
}

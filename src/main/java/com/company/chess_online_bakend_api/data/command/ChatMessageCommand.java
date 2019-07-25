package com.company.chess_online_bakend_api.data.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageCommand extends BaseEntityCommand {

    private Long roomId;
    private String username;
    private Long userId;

    private Long timestamp;

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

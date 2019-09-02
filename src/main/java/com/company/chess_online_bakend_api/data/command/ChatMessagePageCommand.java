/*
 * 8/24/19, 3:16 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ChatMessagePageCommand {

    @ApiModelProperty("Page number (starts from 0)")
    private Integer page;

    @ApiModelProperty("Total message count")
    private Long totalMessages;

    @ApiModelProperty("Found messages")
    private List<ChatMessageCommand> messages;

}

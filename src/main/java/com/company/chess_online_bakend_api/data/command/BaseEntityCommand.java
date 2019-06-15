package com.company.chess_online_bakend_api.data.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntityCommand {
    private Long id;

    private LocalDateTime created;

    private LocalDateTime updated;
}

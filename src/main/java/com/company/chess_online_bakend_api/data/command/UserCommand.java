package com.company.chess_online_bakend_api.data.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCommand extends BaseEntityCommand {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String firstName;
    private String lastName;

    @Email
    private String email;

    @Builder
    public UserCommand(Long id, @NotEmpty String username,
                       @NotEmpty String password, String firstName, String lastName, @Email String email) {

        super(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}

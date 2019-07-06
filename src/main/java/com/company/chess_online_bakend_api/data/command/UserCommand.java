package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.validation.constraint.UniqueUsernameConstraint;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidUsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCommand extends BaseEntityCommand {

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    @ValidUsernameConstraint
    @UniqueUsernameConstraint
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 20 characters long")
    private String password;

    private String firstName;

    private String lastName;

    @NotEmpty(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
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

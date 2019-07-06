package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.validation.constraint.ValidUsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCommand extends BaseEntityCommand {

    @NotEmpty
    @Min(3)
    @Max(15)
    @ValidUsernameConstraint
    private String username;

    @NotEmpty
    @Min(3)
    private String password;

    @Max(20)
    private String firstName;
    @Max(20)
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

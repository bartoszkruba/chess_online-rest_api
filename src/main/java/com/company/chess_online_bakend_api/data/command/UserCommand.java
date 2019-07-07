package com.company.chess_online_bakend_api.data.command;

import com.company.chess_online_bakend_api.data.validation.constraint.UniqueUsernameConstraint;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidPasswordConstraint;
import com.company.chess_online_bakend_api.data.validation.constraint.ValidUsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
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

    public static final String USERNAME_NOT_EMPTY_MESSAGE = "Username cannot be empty";
    public static final String USERNAME_SIZE_MESSAGE = "Username must be between 3 and 20 characters long";

    public static final String PASSWORD_NOT_EMPTY_MESSAGE = "Password cannot be empty";
    public static final String PASSWORD_NOT_SIZE_MESSAGE = "Password must be between 3 and 20 characters long";

    public static final String EMAIL_NOT_EMPTY_MESSAGE = "Email cannot be blank";
    public static final String EMAIL_NOT_VALID_MESSAGE = "Email is not valid";

    @NotEmpty(message = USERNAME_NOT_EMPTY_MESSAGE)
    @Size(min = 3, max = 30, message = USERNAME_SIZE_MESSAGE)
    @ValidUsernameConstraint
    @UniqueUsernameConstraint
    @ApiModelProperty(required = true)
    private String username;

    @NotEmpty(message = PASSWORD_NOT_EMPTY_MESSAGE)
    @Size(min = 3, max = 30, message = PASSWORD_NOT_SIZE_MESSAGE)
    @ValidPasswordConstraint
    @ApiModelProperty(required = true)
    private String password;

    // TODO: 2019-07-06 Create some custom validators for firstname and lastname
    private String firstName;

    private String lastName;

    @NotEmpty(message = EMAIL_NOT_EMPTY_MESSAGE)
    @Email(message = EMAIL_NOT_VALID_MESSAGE)
    @ApiModelProperty(required = true)
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

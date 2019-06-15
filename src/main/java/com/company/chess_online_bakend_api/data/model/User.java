package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true)
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    @Email
    private String email;

    // @Lob Used columns with a lot of data
    @Lob
    private Byte[] profileImage;

    @Builder
    public User(Long id, LocalDateTime created, LocalDateTime updated, @NotEmpty String username,
                @NotEmpty String password, String firstName, String lastName, @Email String email,
                Byte[] profileImage) {
        super(id, created, updated);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImage = profileImage;
    }
}

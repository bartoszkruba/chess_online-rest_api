/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(nullable = false, unique = true)
    @NotEmpty
    private String username;

    @Column(nullable = false)
    @NotEmpty
    private String password;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    @Email
    private String email;

    // @Lob Used for large objects
    @Lob
    private Byte[] profileImage;

    @OneToMany(fetch = FetchType.LAZY)
    private List<WebSocketId> socketConnections;

    @Builder
    public User(Long id, LocalDateTime created, LocalDateTime updated, @NotEmpty String username,
                @NotEmpty String password, String firstName, String lastName, @Email String email,
                Byte[] profileImage, Set<Role> roles) {
        super(id, created, updated);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImage = profileImage;
        this.roles = Objects.requireNonNullElseGet(roles, HashSet::new);
    }

    public User addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);

        return this;
    }
}

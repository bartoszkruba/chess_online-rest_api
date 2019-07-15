package com.company.chess_online_bakend_api.data.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(unique = true)
    String description;

    @Override
    @Transactional
    public String getAuthority() {
        return description;
    }

    @Builder
    public Role(Long id, LocalDateTime created, LocalDateTime updated, String description) {
        super(id, created, updated);
        this.description = description;
    }
}

package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.data.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByDescription(String description);
}
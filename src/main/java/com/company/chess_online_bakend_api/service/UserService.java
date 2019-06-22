package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.model.User;

public interface UserService extends CrudService<User, Long> {

    User findByUsername(String username);
}

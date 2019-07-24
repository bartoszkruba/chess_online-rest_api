package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.UserCommand;

public interface UserService extends CrudService<UserCommand, Long> {

    UserCommand findByUsername(String username);
}

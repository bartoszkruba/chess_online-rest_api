package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.UserCommand;

import java.util.Collection;

public interface AuthenticationService {

    Collection<String> getRolesForUser(String username);

    UserCommand registerNewUser(UserCommand userCommand);
}

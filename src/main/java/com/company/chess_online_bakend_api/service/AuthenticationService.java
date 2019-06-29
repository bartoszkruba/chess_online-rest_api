package com.company.chess_online_bakend_api.service;

import java.util.Collection;

public interface AuthenticationService {

    Collection<String> getRolesForUser(String username);
}

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.UserCommand;

import java.util.Collection;

public interface AuthenticationService {

    Collection<String> getRolesForUser(String username);

    UserCommand registerNewUser(UserCommand userCommand);
}

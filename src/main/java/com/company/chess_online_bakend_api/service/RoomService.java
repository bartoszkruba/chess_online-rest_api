/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.RoomCommand;

import java.util.Set;

public interface RoomService extends CrudService<RoomCommand, Long> {

    long getRoomCount();

    Set<RoomCommand> getRoomPage(int page);

    RoomCommand createNewRoom(RoomCommand roomCommand);
}

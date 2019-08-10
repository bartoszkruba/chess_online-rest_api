/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.validation.validator;

import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.validation.constraint.UniqueRoomNameConstraint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Slf4j
public class UniqueRoomNameValidator
        implements ConstraintValidator<UniqueRoomNameConstraint, String> {

    private final RoomRepository roomRepository;

    @Autowired
    public UniqueRoomNameValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void initialize(UniqueRoomNameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String roomName, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("Checking if room name is unique");

        if (roomName == null) {
            log.debug("Room name is null");
            return true;
        }

        Optional<Room> roomOptional = roomRepository.findByNameLike(roomName);

        if (roomOptional.isPresent()) {
            log.debug("Room name already exists");
            return false;
        }

        return true;
    }
}

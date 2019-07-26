/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.validation.validator;

import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UniqueRoomNameValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    UniqueRoomNameValidator uniqueRoomNameValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void isValid() {
        when(roomRepository.findByNameLike(anyString())).thenReturn(Optional.empty());

        boolean validationResult = uniqueRoomNameValidator.isValid("someUsername", constraintValidatorContext);

        assertTrue(validationResult);

        verify(roomRepository, times(1)).findByNameLike(anyString());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void usernameExists() {
        when(roomRepository.findByNameLike(anyString())).thenReturn(Optional.of(Room.builder().build()));

        boolean validationResult = uniqueRoomNameValidator.isValid("someUsername", constraintValidatorContext);

        assertFalse(validationResult);

        verify(roomRepository, times(1)).findByNameLike(anyString());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void usernameNull() {
        boolean validationResult = uniqueRoomNameValidator.isValid(null, constraintValidatorContext);

        assertTrue(validationResult);

        verifyZeroInteractions(roomRepository);
    }

}
package com.company.chess_online_bakend_api.data.validation.validator;

import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
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
import static org.mockito.Mockito.when;

class UniqueUsernameValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UniqueUsernameValidator uniqueUsernameValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValid() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        boolean validationResult = uniqueUsernameValidator.isValid("someUsername", constraintValidatorContext);


        assertTrue(validationResult);
    }

    @Test
    void usernameExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(User.builder().build()));

        boolean validationResult = uniqueUsernameValidator.isValid("someUsername", constraintValidatorContext);

        assertFalse(validationResult);
    }

    @Test
    void usernameNull() {
        boolean validationResult = uniqueUsernameValidator.isValid(null, constraintValidatorContext);

        assertTrue(validationResult);
    }
}
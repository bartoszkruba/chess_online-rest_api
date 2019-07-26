/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.validation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsernameValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    UsernameValidator usernameValidator = new UsernameValidator();

    @BeforeEach
    void setUp() {
    }

    @Test
    void isValid() {
        String validUsername = "john69";

        boolean validation_result = usernameValidator.isValid(validUsername, constraintValidatorContext);

        assertTrue(validation_result);
    }

    @Test
    void usernameNotValid() {
        String validUsername = " joh_n 69   ";

        boolean validation_result = usernameValidator.isValid(validUsername, constraintValidatorContext);

        assertFalse(validation_result);
    }

    @Test
    void usernameNull() {
        boolean validation_result = usernameValidator.isValid(null, constraintValidatorContext);

        assertTrue(validation_result);
    }
}
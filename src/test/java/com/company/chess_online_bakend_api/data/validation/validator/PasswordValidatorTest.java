/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.validation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValid() {
        String validPassword = "ThisIsValidPassword";

        boolean validationResult = passwordValidator.isValid(validPassword, constraintValidatorContext);

        assertTrue( validationResult);
    }

    @Test
    void passwordNull() {
        boolean validationResult = passwordValidator.isValid(null, constraintValidatorContext);

        assertTrue(validationResult);
    }

    @Test
    void invalidPassword() {
        String invalidPassword = "    thi is invalidPassword ^**-äääööafdsåöfsa";

        boolean validationResult = passwordValidator.isValid(invalidPassword, constraintValidatorContext);

        assertFalse(validationResult);
    }
}
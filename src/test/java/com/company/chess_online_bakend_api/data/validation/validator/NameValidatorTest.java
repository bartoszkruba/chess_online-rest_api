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

class NameValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private NameValidator nameValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValid() {
        String validPassword = "This Is Valid Name";

        boolean validationResult = nameValidator.isValid(validPassword, constraintValidatorContext);

        assertTrue(validationResult);
    }

    @Test
    void nameNull() {
        boolean validationResult = nameValidator.isValid(null, constraintValidatorContext);

        assertTrue(validationResult);
    }

    @Test
    void invalidName() {
        String invalidPassword = "¥¥¥¥¥ sasadsdas däösddswe%%%%as this adsdfdas is ***'sd' invaid name";

        boolean validationResult = nameValidator.isValid(invalidPassword, constraintValidatorContext);

        assertFalse(validationResult);
    }

}
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

class PositionValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    PositionValidator positionValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testValidateNull() {
        assertTrue(positionValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void testInvalidLength() {
        assertFalse(positionValidator.isValid("A2A", constraintValidatorContext));
    }

    @Test
    void testEmptyString() {
        assertFalse(positionValidator.isValid("", constraintValidatorContext));
    }

    @Test
    void testInvalidHorizontalPosition() {
        assertFalse(positionValidator.isValid("K1", constraintValidatorContext));
    }

    @Test
    void testInvalidVerticalPosition() {
        assertFalse(positionValidator.isValid("a9", constraintValidatorContext));
    }

    @Test
    void testValidPosition() {
        assertTrue(positionValidator.isValid("f3", constraintValidatorContext));
    }
}
package com.company.chess_online_bakend_api.data.validation.validator;

import com.company.chess_online_bakend_api.data.validation.constraint.ValidPositionConstraint;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@NoArgsConstructor
public class PositionValidator implements ConstraintValidator<ValidPositionConstraint, String> {

    private final List<String> horizontalPositions = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");
    private final List<String> vertizalPositions = Arrays.asList("1", "2", "3", "4", "5", "6", "7");

    @Override
    public void initialize(ValidPositionConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String position, ConstraintValidatorContext constraintValidatorContext) {

        if (position == null) {
            return true;
        }

        if (position.length() != 2) {
            return false;
        }

        return validHorizontalPosition(position.substring(0, 1)) &&
                validVerticalPosition(position.substring(1, 2));
    }

    private boolean validHorizontalPosition(String position) {
        position = position.toUpperCase();

        if (horizontalPositions.contains(position)) {
            return true;
        }

        return false;
    }

    private boolean validVerticalPosition(String position) {
        if (vertizalPositions.contains(position)) {
            return true;
        }

        return false;
    }
}

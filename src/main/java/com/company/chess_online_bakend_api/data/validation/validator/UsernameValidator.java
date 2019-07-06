package com.company.chess_online_bakend_api.data.validation.validator;


import com.company.chess_online_bakend_api.data.validation.constraint.ValidUsernameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<ValidUsernameConstraint, String> {

    @Override
    public void initialize(ValidUsernameConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

        Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }
}

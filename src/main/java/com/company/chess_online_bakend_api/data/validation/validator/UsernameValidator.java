package com.company.chess_online_bakend_api.data.validation.validator;


import com.company.chess_online_bakend_api.data.validation.constraint.ValidUsernameConstraint;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@NoArgsConstructor
public class UsernameValidator implements ConstraintValidator<ValidUsernameConstraint, String> {

    @Override
    public void initialize(ValidUsernameConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

        if (username == null) {
            return false;
        }

        log.debug("Validating username");

        Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }
}

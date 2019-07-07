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
        log.debug("Validating username");
        if (username == null) {
            log.debug("Username is null");
            return true;
        }


        Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");
        Matcher matcher = pattern.matcher(username);

        if (matcher.matches()) {
            return true;
        } else {
            log.debug("Username is not valid");
            return false;
        }
    }
}

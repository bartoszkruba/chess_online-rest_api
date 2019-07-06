package com.company.chess_online_bakend_api.data.validation.validator;

import com.company.chess_online_bakend_api.data.validation.constraint.ValidPasswordConstraint;
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
public class PasswordValidator implements ConstraintValidator<ValidPasswordConstraint, String> {

    @Override
    public void initialize(ValidPasswordConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return true;
        }

        log.debug("Validating password");

        Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");

        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}

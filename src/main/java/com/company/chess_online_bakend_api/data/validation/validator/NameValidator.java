/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.validation.validator;

import com.company.chess_online_bakend_api.data.validation.constraint.ValidNameConstraint;
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
public class NameValidator implements ConstraintValidator<ValidNameConstraint, String> {


    @Override
    public void initialize(ValidNameConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("Validating name");
        if (name == null) {
            log.debug("name is null");
            return true;
        }


        Pattern pattern = Pattern.compile("^[A-Za-z0-9_ -]*$");
        Matcher matcher = pattern.matcher(name);

        if (matcher.matches()) {
            return true;
        } else {
            log.debug("name is not valid");
            return false;
        }
    }
}

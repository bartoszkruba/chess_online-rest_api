package com.company.chess_online_bakend_api.data.validation.constraint;

import com.company.chess_online_bakend_api.data.validation.validator.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNameConstraint {

    String ERROR_MESSAGE = "Invalid name";

    String message() default ERROR_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

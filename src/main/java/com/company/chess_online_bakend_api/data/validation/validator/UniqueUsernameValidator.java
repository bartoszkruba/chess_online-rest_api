package com.company.chess_online_bakend_api.data.validation.validator;

import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.data.validation.constraint.UniqueUsernameConstraint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Slf4j
public class UniqueUsernameValidator
        implements ConstraintValidator<UniqueUsernameConstraint, String> {

    private final UserRepository userRepository;

    @Autowired
    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueUsernameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("Checking if username is unique");

        if (username == null) {
            log.debug("Username is null");
            return true;
        }

        Optional<User> userOptional = userRepository.findByUsernameLike(username);

        if (userOptional.isEmpty()) {
            return true;
        } else {
            log.debug("Username already exists");
            return false;
        }
    }
}

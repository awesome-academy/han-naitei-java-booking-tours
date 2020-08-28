package com.bookingTour.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bookingTour.service.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

    @Autowired
    UserServiceImp userServiceImp;

    @Override
    public void initialize(final UniqueUserName constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String userName, final ConstraintValidatorContext context) {
        return userName != null && userServiceImp.findUserByUserName(userName) == null;
    }
}

package com.bookingTour.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bookingTour.service.imp.UserServiceImp;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, Object> {

    private static final Logger log = LoggerFactory.getLogger(UniqueUserNameValidator.class);

    private String name;
    private String message;

    @Autowired
    UserServiceImp userServiceImp;

    @Override
    public void initialize(final UniqueUserName constraintAnnotation) {
        this.name = constraintAnnotation.name();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object user, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            String userName = BeanUtils.getProperty(user, name);
            String ids = BeanUtils.getProperty(user, "id");
            if (StringUtils.isEmpty(userName)) {
                valid = false;
            } else {
                Long id = null;
                if (ids != null) {
                    id = Long.parseLong(ids);
                }
                valid = !userServiceImp.existingUserName(userName, id);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(name).addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}

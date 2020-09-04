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

public class UniqueFieldValidator implements ConstraintValidator<UniqueField, Object> {

    private static final Logger log = LoggerFactory.getLogger(UniqueFieldValidator.class);

    private String field;
    private String column;
    private String table;
    private String message;

    @Autowired
    UserServiceImp userServiceImp;

    @Override
    public void initialize(final UniqueField constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.column = constraintAnnotation.column();
        this.table = constraintAnnotation.table();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            String value = BeanUtils.getProperty(object, field);
            String ids = BeanUtils.getProperty(object, "id");
            if (StringUtils.isEmpty(value)) {
                valid = false;
            } else {
                Long id = null;
                if (ids != null) {
                    id = Long.parseLong(ids);
                }
                valid = !userServiceImp.checkExisted(id, field, value, column);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(field).addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}

package com.bookingTour.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueUserNameValidator.class)
@Documented
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface UniqueUserName {
    String message() default "The username is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

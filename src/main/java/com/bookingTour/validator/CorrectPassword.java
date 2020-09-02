package com.bookingTour.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = CorrectPasswordValidator.class)
@Documented
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface CorrectPassword {
    String message() default "The old password is incorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String name();
}

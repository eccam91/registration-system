package com.engeto.registrationsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPersonIDValidator.class)
@Documented
public @interface ValidPersonID {

    String message() default "Invalid personID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int length() default 12;

    String pattern() default "[a-zA-Z0-9]+";
}
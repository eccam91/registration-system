package com.engeto.registrationsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class ValidPersonIDValidator implements ConstraintValidator<ValidPersonID, String> {

    private static final Logger logger = LoggerFactory.getLogger(ValidPersonIDValidator.class);
    private int length;
    private Pattern pattern;

    @Override
    public void initialize(ValidPersonID constraintAnnotation) {
        this.length = constraintAnnotation.length();
        this.pattern = Pattern.compile(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(String personID, ConstraintValidatorContext context) {
        if (personID == null) {
            logger.warn("Validation failed: personID is null");
            return false;
        }

        int personIDLength = personID.length();
        boolean isValidLength = personIDLength >= 12 && personIDLength <= length;

        if (!isValidLength) {
            logger.warn("Validation failed: personID length is invalid");
            return false;
        }

        if (!pattern.matcher(personID).matches()) {
            logger.warn("Validation failed: personID pattern is invalid");
            return false;
        }

        logger.info("Validation successful: personID is valid");
        return true;
    }
}
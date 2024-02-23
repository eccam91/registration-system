package com.engeto.registrationsystem.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidPersonIDValidatorTest {

    private Validator validator;
    private static final Logger logger = LoggerFactory.getLogger(ValidPersonIDValidatorTest.class);

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testValidPersonID() {
        TestClass testClass = new TestClass("ValidID12345");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);
        assertTrue(violations.isEmpty());
        logger.info("Valid personID test passed");
    }

    @Test
    void testInvalidPersonID_Length() {
        TestClass testClass = new TestClass("ShortID");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        ConstraintViolation<TestClass> violation = violations.iterator().next();
        assertEquals("Invalid personID", violation.getMessage());
        logger.info("Invalid personID length test passed");
    }

    @Test
    void testInvalidPersonID_Pattern() {
        TestClass testClass = new TestClass("Invalid@ID12");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        ConstraintViolation<TestClass> violation = violations.iterator().next();
        assertEquals("Invalid personID", violation.getMessage());
        logger.info("Invalid personID pattern test passed");
    }

    private record TestClass(@ValidPersonID String personID) {
    }
}
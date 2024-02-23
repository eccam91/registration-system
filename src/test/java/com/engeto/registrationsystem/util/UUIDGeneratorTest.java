package com.engeto.registrationsystem.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDGeneratorTest {

    private static final Logger logger = LoggerFactory.getLogger(UUIDGeneratorTest.class);

    @Test
    void testGenerateType1UUID() {
        UUID uuid1 = UUIDGenerator.generateType1UUID();
        UUID uuid2 = UUIDGenerator.generateType1UUID();

        assertNotEquals(uuid1, uuid2);
        logger.info("UUIDs generated successfully");

        assertEquals(1, uuid1.version());
        assertEquals(1, uuid2.version());
        logger.info("UUID version validation successful");

        assertEquals(2, uuid1.variant());
        assertEquals(2, uuid2.variant());
        logger.info("UUID variant validation successful");
    }
}
package com.engeto.registrationsystem.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);

    @Test
    void testEquals() {
        UUID uuid = UUID.randomUUID();

        User user1 = new User("John", "Doe", "12345");
        user1.setId(1L);
        user1.setUuid(uuid);

        User user2 = new User("John", "Doe", "12345");
        user2.setId(1L);
        user2.setUuid(uuid);

        User user3 = new User("Jane", "Smith", "67890");
        user3.setId(2L);
        user3.setUuid(UUID.randomUUID());

        assertEquals(user1, user2);
        logger.info("Equality test passed for user1 and user2");

        user2.setUuid(UUID.randomUUID());
        assertNotEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user2, user3);
        logger.info("Inequality tests passed successfully");
    }

    @Test
    void testHashCode() {
        UUID uuid = UUID.randomUUID();

        User user1 = new User("John", "Doe", "12345");
        user1.setId(1L);
        user1.setUuid(uuid);

        User user2 = new User("John", "Doe", "12345");
        user2.setId(1L);
        user2.setUuid(uuid);

        User user3 = new User("Jane", "Smith", "67890");
        user3.setId(2L);
        user3.setUuid(UUID.randomUUID());

        assertEquals(user1.hashCode(), user2.hashCode());
        logger.info("Equal hash codes test passed");

        user2.setUuid(UUID.randomUUID());
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user3.hashCode());
        assertNotEquals(user2.hashCode(), user3.hashCode());
        logger.info("Inequal hash codes test passed");
    }

    @Test
    void testToString() {
        User user = new User("John", "Doe", "12345");
        user.setId(1L);
        user.setUuid(UUID.randomUUID());

        String expectedToString = "User{id=1, name='John', surname='Doe', personID='12345', uuid='" + user.getUuid() + "'}";
        assertEquals(expectedToString, user.toString());
        logger.info("ToString test passed");
    }
}


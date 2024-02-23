package com.engeto.registrationsystem.service;

import com.engeto.registrationsystem.model.User;
import com.engeto.registrationsystem.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(UserServiceTest.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser() {
        User user = new User("John", "Doe", "12345");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getUuid());

        verify(userRepository, times(1)).save(any(User.class));
        logger.info("CreateUser test passed, created: {}", createdUser);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User("John", "Doe", "12345");
        user.setUuid(UUID.randomUUID());
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getUserById(userId);

        assertEquals(user, retrievedUser);

        verify(userRepository, times(1)).findById(userId);
        logger.info("GetUserById test passed, retrieved: {}", retrievedUser);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User("John", "Doe", "12345");
        User user2 = new User("Jane", "Smith", "67890");
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getAllUsers();

        assertEquals(users, retrievedUsers);

        verify(userRepository, times(1)).findAll();
        logger.info("GetAllUsers test passed, retrieved: {}", retrievedUsers);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("John", "Doe", "12345");
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertEquals(user, updatedUser);

        verify(userRepository, times(1)).save(user);
        logger.info("UpdateUser test passed, updated: {}", updatedUser);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        boolean isDeleted = userService.deleteUser(userId);

        assertTrue(isDeleted);

        verify(userRepository, times(1)).deleteById(userId);
        logger.info("DeleteUser test passed, deleted user by ID: {}", userId);
    }

    @Test
    public void testAuthenticateUser() {
        User credentials = new User("John", "Doe", "12345");
        User authenticatedUser = new User("John", "Doe", "12345");
        authenticatedUser.setId(1L);

        when(userRepository.findByNameAndSurnameAndPersonID(credentials.getName(), credentials.getSurname(), credentials.getPersonID()))
                .thenReturn(authenticatedUser);

        User retrievedUser = userService.authenticateUser(credentials);

        assertEquals(authenticatedUser, retrievedUser);

        verify(userRepository, times(1)).findByNameAndSurnameAndPersonID(credentials.getName(), credentials.getSurname(), credentials.getPersonID());
        logger.info("AuthenticateUser test passed, authenticated: {}", retrievedUser);
    }
}
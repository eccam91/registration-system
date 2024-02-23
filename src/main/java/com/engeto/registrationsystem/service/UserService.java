package com.engeto.registrationsystem.service;

import com.engeto.registrationsystem.model.User;
import com.engeto.registrationsystem.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        user.setUuid(UUID.randomUUID());
        logger.info("Creating user: {}", user);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            logger.info("Retrieved user by ID {}: {}", id, user);
        } else {
            logger.warn("User not found with ID: {}", id);
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        logger.info("Retrieved all users: {}", users);
        return users;
    }

    public User updateUser(User user) {
        logger.info("Updating user: {}", user);
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Deleted user by ID {}", id);
            return true;
        } else {
            logger.warn("User not found with ID: {}", id);
            return false;
        }
    }

    public User authenticateUser(User credentials) {
        User authenticatedUser = userRepository.findByNameAndSurnameAndPersonID(
                credentials.getName(), credentials.getSurname(), credentials.getPersonID());
        if (authenticatedUser != null) {
            logger.info("User authenticated successfully: {}", authenticatedUser);
        } else {
            logger.warn("User authentication failed for credentials: {}", credentials);
        }
        return authenticatedUser;
    }
}


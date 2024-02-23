package com.engeto.registrationsystem.controller;

import com.engeto.registrationsystem.model.User;
import com.engeto.registrationsystem.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        logger.info("Received request to create user: {}", user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid Personal ID");
            bindingResult.getAllErrors().forEach(error -> logger.error("Validation error: {}", error));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            User createdUser = userService.createUser(user);
            logger.info("User created successfully: {}", createdUser);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Personal ID already exists");
            logger.error("Data integrity violation occurred: {}", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private User mapUserToBasicInfoUser(User user) {
        User basicInfoUser = new User();
        basicInfoUser.setId(user.getId());
        basicInfoUser.setName(user.getName());
        basicInfoUser.setSurname(user.getSurname());
        return basicInfoUser;
    }

    @GetMapping("user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean detail) {
        logger.info("Received request to get user by ID: {}", id);

        User user = userService.getUserById(id);

        if (user != null) {
            if (detail) {
                logger.info("Returning detailed user information: {}", user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                logger.info("Returning basic user information: {}", mapUserToBasicInfoUser(user));
                return new ResponseEntity<>(mapUserToBasicInfoUser(user), HttpStatus.OK);
            }
        } else {
            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("users")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "false") Boolean detail) {
        logger.info("Received request to get all users");

        List<User> users = userService.getAllUsers();

        if (!users.isEmpty()) {
            List<User> resultUsers = new ArrayList<>();

            for (User user : users) {
                if (detail) {
                    logger.info("Returning detailed user information: {}", user);
                    resultUsers.add(user);
                } else {
                    logger.info("Returning basic user information: {}", mapUserToBasicInfoUser(user));
                    resultUsers.add(mapUserToBasicInfoUser(user));
                }
            }

            return new ResponseEntity<>(resultUsers, HttpStatus.OK);
        } else {
            logger.warn("No users found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("user")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        logger.info("Received request to update user with ID: {}", user.getId());

        User existingUser = userService.getUserById(user.getId());

        if (existingUser == null) {
            logger.warn("User not found with ID: {}", user.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID:  " + user.getId() + " not found");

        }

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());

        User updatedUser = userService.updateUser(existingUser);

        logger.info("User updated successfully: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody User credentials) {
        logger.info("Received request to login user with credentials: {}", credentials);

        User authenticatedUser = userService.authenticateUser(credentials);

        if (authenticatedUser != null) {
            logger.info("User authentication successful: {}", authenticatedUser);
            return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
        } else {
            logger.warn("Invalid credentials for login attempt: {}", credentials);
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Received request to delete user with ID: {}", id);

        if (userService.deleteUser(id)) {
            logger.info("User deleted successfully with ID: {}", id);
            return ResponseEntity.ok("User with ID " + id + " deleted successfully");
        } else {
            logger.warn("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: User with the ID " + id + " does not exist");
        }
    }
}
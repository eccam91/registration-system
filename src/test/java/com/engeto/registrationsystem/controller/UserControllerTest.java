package com.engeto.registrationsystem.controller;

import com.engeto.registrationsystem.model.User;
import com.engeto.registrationsystem.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private static final Logger logger = LogManager.getLogger(UserControllerTest.class);
    private static final String VALID_USER_JSON = "{\"name\":\"John\",\"surname\":\"Doe\",\"personID\":\"123456789012\"}";
    private static final String INVALID_USER_JSON = "{\"name\":\"John\",\"surname\":\"Doe\",\"personID\":\"123\"}";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("John", "Doe", "123456789012");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                        .content(VALID_USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personID").value("123456789012"));

        verify(userService, times(1)).createUser(eq(user));

        logger.info("CreateUser test executed successfully");
    }

    @Test
    public void testCreateUser_InvalidPersonID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                        .content(INVALID_USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid Personal ID"));

        verify(userService, never()).createUser(any(User.class));

        logger.info("CreateUser_InvalidPersonID test executed successfully");
    }

    @Test
    public void testGetUserById() throws Exception {
        Long userId = 1L;
        User user = new User("John", "Doe", "12345");

        when(userService.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", userId)
                        .param("detail", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personID").value("12345"));

        verify(userService, times(1)).getUserById(eq(userId));

        logger.info("GetUserById test executed successfully");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User("John", "Doe", "12345");
        User user2 = new User("Jane", "Smith", "67890");
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("detail", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].personID").value("12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].personID").value("67890"));

        verify(userService, times(1)).getAllUsers();

        logger.info("GetAllUsers test executed successfully");
    }

    @Test
    public void testUpdateUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John");
        existingUser.setSurname("Doe");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Jane");
        updatedUser.setSurname("Smith");

        when(userService.getUserById(1L)).thenReturn(existingUser);
        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Jane\",\"surname\":\"Smith\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(eq(updatedUser));

        logger.info("UpdateUser test executed successfully");
    }

    @Test
    public void testLoginUser() throws Exception {
        User user = new User("John", "Doe", "12345");

        when(userService.authenticateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .content("{\"name\":\"John\",\"surname\":\"Doe\",\"personID\":\"12345\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personID").value("12345"));

        verify(userService, times(1)).authenticateUser(eq(user));

        logger.info("LoginUser test executed successfully");
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));

        verify(userService, times(1)).deleteUser(eq(1L));

        logger.info("DeleteUser test executed successfully");
    }
}
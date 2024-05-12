package com.alejandro.app.rest.Controllers;


import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void testUpdateUser() throws Exception {
        long userId = 1L;
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");

        Mockito.when(userService.updateUser(userId, updatedUser)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
        long userId = 1L;

        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);

    }

    @Test
    public void testRegisterUser() throws Exception {
        User newUser = new User();
        newUser.setEmail("test@gmail.com");
        newUser.setPassword("parola");
        Mockito.when(userService.registerUser(newUser)).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("existing@gmail.com");
        existingUser.setPassword("password");

        Mockito.when(userService.registerUser(any(User.class)))
                .thenThrow(new RuntimeException("User with this email already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(existingUser)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testLoginUser() throws Exception {
        User loginUser = new User();
        loginUser.setEmail("test@gmail.com");
        loginUser.setPassword("password");

        Mockito.when(userService.loginUser(loginUser)).thenReturn(loginUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

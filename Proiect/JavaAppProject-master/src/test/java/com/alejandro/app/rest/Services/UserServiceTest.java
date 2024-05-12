package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testLoginUser_ValidCredentials() {
        // Mock user with valid credentials
        User loginUser = new User();
        loginUser.setEmail("test@example.com");
        loginUser.setPassword("password");

        User existingUser = new User();
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");

        when(userRepository.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword()))
                .thenReturn(Optional.of(existingUser));

        User result = userService.loginUser(loginUser);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("password", result.getPassword());

        verify(userRepository, times(1)).findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword());
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        // Mock user with invalid credentials
        User loginUser = new User();
        loginUser.setEmail("test@example.com");
        loginUser.setPassword("wrongpassword");

        when(userRepository.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.loginUser(loginUser));

        verify(userRepository, times(1)).findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword());
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        User userToUpdate = new User();
        userToUpdate.setEmail("updated@example.com");
        userToUpdate.setPassword("newpassword");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userId, userToUpdate);

        assertNotNull(updatedUser);
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("newpassword", updatedUser.getPassword());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("user1@example.com", "password1"));
        userList.add(new User("user2@example.com", "password2"));

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByEmail_UserExists() {
        String email = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        User result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_UserNotExists() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(email));

        verify(userRepository, times(1)).findByEmail(email);
    }


}

package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User registeredUser) {
        if (userRepository.findByEmail(registeredUser.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setEmail(registeredUser.getEmail());
        user.setPassword(registeredUser.getPassword());
        return userRepository.save(user);
    }

    public User loginUser(User loginUser) {
        return userRepository.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        // If the user exists, return it; otherwise, throw an exception or handle as needed
        return userOptional.orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));    }
}

package com.fullstack.backend.controller;

import com.fullstack.backend.model.User;
import com.fullstack.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    ResponseEntity<String> newUser(@RequestBody User newUser) {
        String password = newUser.getPassword();

        // Check if password meets criteria
        if (!validatePassword(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Password must be at least 6 characters long and contain at least one special character");
        }

        // Proceed with user creation
        userRepository.save(newUser);
        return ResponseEntity.ok("User created successfully");
    }

    // Example password validation method
    private boolean validatePassword(String password) {
        return password.matches("^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*[a-zA-Z0-9]).{6,}$");
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        User user = userRepository.findByEmail(loginUser.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        }
        if (!user.getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        
        return ResponseEntity.ok(user);
    }
}

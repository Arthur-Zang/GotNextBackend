package com.example.GotNext.Controllers;

import com.example.GotNext.Collections.User;
import com.example.GotNext.Requests.LoginRequest;
import com.example.GotNext.Requests.RegistrationRequest;
import com.example.GotNext.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getEmail());
        // Validate the username and password
        User user = userRepository.findUserByEmail(loginRequest.getEmail());
        System.out.println(user);
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId().toString());
        userResponse.put("username", user.getUsername());
        userResponse.put("activeCourt", user.getActiveCourt());
        userResponse.put("email", user.getEmail());

        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        System.out.println(registrationRequest.getUsername());
        // Validate registration request
        if (userRepository.findByUsername(registrationRequest.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }


        // Create a new user
        User newUser = new User(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                registrationRequest.getUsername(),
                registrationRequest.getPassword(),
                LocalDateTime.now()
        );

        // Save the user to the database
        userRepository.save(newUser);

        return ResponseEntity.ok("Registration successful");
    }
}
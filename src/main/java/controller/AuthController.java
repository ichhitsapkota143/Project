package controller;

import model.User;
import repository.UserRepository;
import util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        System.out.println("Login attempt for: " + loginRequest.getUsername());
        User user = userRepo.findByUsername(loginRequest.getUsername());

        if (user == null) {
            System.out.println("User not found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        boolean passwordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        System.out.println("Password match: " + passwordMatch);

        if (passwordMatch) {
            return jwtUtil.generateToken(user.getUsername());
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    @PostMapping("/register")
    public String register(@RequestBody User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepo.save(newUser);
        return "User registered successfully";
    }
}

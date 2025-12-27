package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.AuthUser;
import com.example.taskmanagement.model.Role;
import com.example.taskmanagement.repository.AuthUserRepository;
import com.example.taskmanagement.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthUserRepository authUserRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public void register(@RequestBody AuthUser user) {
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new IllegalArgumentException("Логин не может быть пустым");

        if (authUserRepository.findByUsername(user.getUsername()).isPresent())
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");

        if (!isPasswordStrong(user.getPassword()))
            throw new IllegalArgumentException("Пароль должен быть не менее 8 символов, содержать цифру и спецсимвол");

        AuthUser newUser = new AuthUser();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.ROLE_USER);

        authUserRepository.save(newUser);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthUser user) {
        AuthUser existing = authUserRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (!passwordEncoder.matches(user.getPassword(), existing.getPassword()))
            throw new IllegalArgumentException("Неверный пароль");

        String token = jwtUtil.generateToken(existing.getUsername());

        return Map.of("token", token);
    }

    private boolean isPasswordStrong(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*\\d.*")
                && password.matches(".*[!@#$%^&*()].*");
    }
}

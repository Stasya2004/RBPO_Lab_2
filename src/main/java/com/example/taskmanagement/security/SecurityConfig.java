package com.example.taskmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF для API (токен защищает)
                .csrf(csrf -> csrf.disable())

                // ---------------- Authorization ----------------
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // регистрация и логин без авторизации
                        .requestMatchers("/projects/**").authenticated()
                        .requestMatchers("/tasks/**").authenticated()
                        .requestMatchers("/comments/**").authenticated()
                        .requestMatchers("/tags/**").authenticated()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // ---------------- JWT Filter ----------------
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

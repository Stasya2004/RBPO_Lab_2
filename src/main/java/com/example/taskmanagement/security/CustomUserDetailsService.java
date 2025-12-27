package com.example.taskmanagement.security;

import com.example.taskmanagement.model.AuthUser;
import com.example.taskmanagement.repository.AuthUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository repository;

    public CustomUserDetailsService(AuthUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AuthUser authUser = repository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Пользователь не найден")
                );

        // Проверка, что пользователь подтверждён админом
        if (!authUser.isEnabled()) {
            throw new UsernameNotFoundException("Пользователь не подтвержден");
        }

        // Возвращаем Spring Security UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(authUser.getUsername())
                .password(authUser.getPassword())
                .roles(authUser.getRole().name().replace("ROLE_", ""))
                .build();
    }
}

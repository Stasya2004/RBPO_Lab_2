package com.example.taskmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "auth_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        }
)
public class AuthUser {

    /* ---------------- ID ---------------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------------- LOGIN ---------------- */

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /* ---------------- PASSWORD ---------------- */

    @Column(nullable = false)
    private String password;

    /* ---------------- ROLE ---------------- */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /* ---------------- CONSTRUCTORS ---------------- */

    public AuthUser() {
    }

    public AuthUser(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /* ---------------- GET / SET ---------------- */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Хранится ТОЛЬКО хэш (BCrypt)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Сюда передаётся уже захэшированный пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

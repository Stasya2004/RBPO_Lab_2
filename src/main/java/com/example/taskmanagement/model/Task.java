// Task.java
package com.example.taskmanagement.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.IN_PROGRESS; // по умолчанию

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({"users"}) // скрываем пользователей проекта
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "task_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"projects"}) // скрываем проекты пользователя
    private List<User> users;

    public Task() {}

    public Task(Long id, String name, String description, Project project, List<User> users) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.project = project;
        this.users = users;
        this.status = TaskStatus.IN_PROGRESS;
    }

    // геттеры и сеттеры

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
}

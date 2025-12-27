package com.example.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonIgnoreProperties({"comments", "users", "project"})
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"projects", "hibernateLazyInitializer", "handler"})
    private User user;

    public Comment() {}

    public Comment(Long id, String text, Task task, User user) {
        this.id = id;
        this.text = text;
        this.task = task;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}


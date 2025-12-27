package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public CommentService(CommentRepository commentRepo,
                          TaskRepository taskRepo,
                          UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    /* ---------------- CREATE ---------------- */
    public Comment create(Comment comment) {
        Long taskId = comment.getTask().getId();
        Long userId = comment.getUser().getId();

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Задача не найдена"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        // Проверка: пользователь должен состоять в проекте задачи
        boolean userInProject = task.getProject().getUsers().stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        if (!userInProject) {
            throw new IllegalArgumentException(
                    "Пользователь " + user.getName() + " не состоит в проекте задачи"
            );
        }

        comment.setTask(task);
        comment.setUser(user);

        return commentRepo.save(comment);
    }

    /* ---------------- READ ---------------- */
    public List<Comment> getAll() {
        return commentRepo.findAll();
    }

    public Comment getById(Long id) {
        return commentRepo.findById(id).orElse(null);
    }

    /* ---------------- UPDATE ---------------- */
    public Comment update(Long id, Comment updated) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));

        comment.setText(updated.getText());

        if (updated.getUser() != null) {
            User user = userRepo.findById(updated.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

            Task task = comment.getTask();

            boolean userInProject = task.getProject().getUsers().stream()
                    .anyMatch(u -> u.getId().equals(user.getId()));

            if (!userInProject) {
                throw new IllegalArgumentException(
                        "Пользователь " + user.getName() + " не состоит в проекте задачи"
                );
            }

            comment.setUser(user);
        }

        return commentRepo.save(comment);
    }

    /* ---------------- DELETE ---------------- */
    public void delete(Long id) {
        commentRepo.deleteById(id);
    }
}

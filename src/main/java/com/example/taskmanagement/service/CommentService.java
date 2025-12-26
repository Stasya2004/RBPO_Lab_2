package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public List<Comment> getAll() {
        return repository.findAll();
    }

    public Comment getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Comment create(Comment comment) {
        return repository.save(comment);
    }

    public Comment update(Long id, Comment updated) {
        Optional<Comment> opt = repository.findById(id);
        if (opt.isPresent()) {
            Comment comment = opt.get();
            comment.setText(updated.getText());
            comment.setTask(updated.getTask());
            comment.setProject(updated.getProject());
            comment.setUser(updated.getUser());
            return repository.save(comment);
        }
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

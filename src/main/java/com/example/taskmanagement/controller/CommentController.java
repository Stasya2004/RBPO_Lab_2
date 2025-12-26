package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Comment> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        return service.create(comment);
    }

    @PutMapping("/{id}")
    public Comment update(@PathVariable Long id, @RequestBody Comment comment) {
        return service.update(id, comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

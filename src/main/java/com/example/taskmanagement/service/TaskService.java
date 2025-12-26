// TaskService.java
package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAll() { return repository.findAll(); }

    public Task getById(Long id) { return repository.findById(id).orElse(null); }

    public Task create(Task task) { return repository.save(task); }

    public Task update(Long id, Task updatedTask) {
        Optional<Task> taskOpt = repository.findById(id);
        if(taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            task.setProject(updatedTask.getProject());
            task.setUsers(updatedTask.getUsers());
            task.setStatus(updatedTask.getStatus()); // ← добавляем обновление статуса
            return repository.save(task);
        }
        return null;
    }


    public void delete(Long id) { repository.deleteById(id); }
}
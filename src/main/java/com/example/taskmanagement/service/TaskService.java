package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    /* ---------------- GET ---------------- */

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task getById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    /* ---------------- CREATE ---------------- */

    public Task create(Task task) {
        Project project = loadAndValidateProject(task);
        task.setProject(project);
        return taskRepository.save(task);
    }

    /* ---------------- UPDATE ---------------- */

    public Task update(Long id, Task updatedTask) {
        Optional<Task> opt = taskRepository.findById(id);
        if (opt.isEmpty()) return null;

        Task task = opt.get();

        Project project = loadAndValidateProject(updatedTask);

        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        task.setProject(project);
        task.setUsers(updatedTask.getUsers());

        return taskRepository.save(task);
    }

    /* ---------------- DELETE ---------------- */

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    /* ---------------- VALIDATION ---------------- */

    private Project loadAndValidateProject(Task task) {
        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new IllegalArgumentException("Задача должна быть привязана к проекту");
        }

        Project project = projectRepository.findById(task.getProject().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Проект не найден")
                );

        if (task.getUsers() != null) {
            List<Long> projectUserIds = project.getUsers()
                    .stream()
                    .map(User::getId)
                    .toList();

            for (User user : task.getUsers()) {
                if (!projectUserIds.contains(user.getId())) {
                    throw new IllegalArgumentException(
                            "Пользователь с id=" + user.getId()
                                    + " не принадлежит проекту id=" + project.getId()
                    );
                }
            }
        }

        return project;
    }
}

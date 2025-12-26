
// ProjectService.java
package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<Project> getAll() { return repository.findAll(); }

    public Project getById(Long id) { return repository.findById(id).orElse(null); }

    public Project create(Project project) { return repository.save(project); }

    public Project update(Long id, Project updatedProject) {
        Optional<Project> projOpt = repository.findById(id);
        if(projOpt.isPresent()) {
            Project project = projOpt.get();
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            project.setUsers(updatedProject.getUsers());
            return repository.save(project);
        }
        return null;
    }

    public void delete(Long id) { repository.deleteById(id); }
}

package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Tag;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TagRepository;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    public TagService(TagRepository tagRepository, TaskRepository taskRepository) {
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
    }

    /* ---------------- CREATE ---------------- */

    public Tag create(Tag tag) {
        Tag savedTag = tagRepository.save(tag);

        if (tag.getTasks() != null) {
            for (Task t : tag.getTasks()) {
                Task realTask = taskRepository.findById(t.getId()).orElse(null);
                if (realTask != null) {
                    realTask.getTags().add(savedTag); // 🔥 ВАЖНО
                    taskRepository.save(realTask);
                }
            }
        }

        return savedTag;
    }

    /* ---------------- READ ---------------- */

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag getById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    /* ---------------- UPDATE ---------------- */

    public Tag update(Long id, Tag updatedTag) {
        Optional<Tag> opt = tagRepository.findById(id);
        if (opt.isEmpty()) return null;

        Tag tag = opt.get();
        tag.setName(updatedTag.getName());

        // ❗ очищаем старые связи
        for (Task task : taskRepository.findAll()) {
            task.getTags().remove(tag);
            taskRepository.save(task);
        }

        // ❗ добавляем новые связи
        if (updatedTag.getTasks() != null) {
            for (Task t : updatedTag.getTasks()) {
                Task realTask = taskRepository.findById(t.getId()).orElse(null);
                if (realTask != null) {
                    realTask.getTags().add(tag); // 🔥 owning side
                    taskRepository.save(realTask);
                }
            }
        }

        return tagRepository.save(tag);
    }

    /* ---------------- DELETE ---------------- */

    public void delete(Long id) {
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag != null) {
            for (Task task : taskRepository.findAll()) {
                task.getTags().remove(tag);
                taskRepository.save(task);
            }
        }
        tagRepository.deleteById(id);
    }
}

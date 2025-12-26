package com.aztrex.procgenjs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository repository;

    public List<Project> getAllProjects() {
        return repository.findAll();
    }

    public Project saveProject(Project project) {
        // Business logic can go here (e.g., validation)
        return repository.save(project);
    }

    public Project getProjectById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }
    
    public void deleteProject(Long id) {
        repository.deleteById(id);
    }
}

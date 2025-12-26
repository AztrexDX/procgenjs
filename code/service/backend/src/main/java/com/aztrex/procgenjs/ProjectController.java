package com.aztrex.procgenjs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @GetMapping
    public List<Project> getAll() {
        return service.getAllProjects();
    }

    @PostMapping
    public Project create(@RequestBody Project project) {
        return service.saveProject(project);
    }

    @GetMapping("/{id}")
    public Project getOne(@PathVariable Long id) {
        return service.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteProject(id);
        return "Project deleted successfully";
    }
}
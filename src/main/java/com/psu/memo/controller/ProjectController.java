package com.psu.memo.controller;

import com.psu.memo.entity.Project;
import com.psu.memo.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.saveProject(project);
    }

    @GetMapping("/{projectName}")
    public List<Project> getProjectTasks(@PathVariable String projectName) {
        return projectService.getProjectsByProjectName(projectName);
    }
}

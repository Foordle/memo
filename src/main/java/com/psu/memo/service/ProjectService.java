package com.psu.memo.service;


import com.psu.memo.entity.Project;
import com.psu.memo.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByProjectName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }
}
package com.psu.memo.service;

import com.psu.memo.dto.ProjectRequestDto;
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

    public Project saveProject(ProjectRequestDto requestDto) {
        Project project = new Project();
        project.setProjectName(requestDto.getProjectName());
        project.setTaskName(requestDto.getTaskName());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        project.setAssignedTo(requestDto.getAssignedTo());
        project.setDependency(requestDto.getDependency());
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByProjectName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. ID: " + id));
    }


    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. ID: " + id);
        }
        projectRepository.deleteById(id);
    }


}

package com.psu.memo.controller;

import com.psu.memo.dto.ProjectRequestDto;
import com.psu.memo.dto.ProjectResponseDto;
import com.psu.memo.entity.Project;
import com.psu.memo.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // 프로젝트 생성 API
    @PostMapping
    public ProjectResponseDto createProject(@RequestBody ProjectRequestDto requestDto) {
        Project project = projectService.saveProject(requestDto);
        return new ProjectResponseDto(project);
    }

    // 프로젝트 전체 조회 API
    @GetMapping
    public List<ProjectResponseDto> getAllProjects() {
        return projectService.getAllProjects().stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 프로젝트명으로 조회 API (작업 리스트)
    @GetMapping("/name/{projectName}")
    public List<ProjectResponseDto> getProjectTasks(@PathVariable String projectName) {
        return projectService.getProjectsByProjectName(projectName).stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 프로젝트 ID로 조회 API
    @GetMapping("/id/{id}")
    public ProjectResponseDto getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return new ProjectResponseDto(project);
    }

    // 프로젝트 삭제 API
    @DeleteMapping("/id/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}

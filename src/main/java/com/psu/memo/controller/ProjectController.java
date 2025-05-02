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

    // 특정 프로젝트 조회 API
    @GetMapping("/{projectName}/tasks")
    public List<ProjectResponseDto> getProjectTasks(@PathVariable("projectName") String projectName) {
        return projectService.getProjectsByProjectName(projectName).stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ProjectResponseDto getProjectById(@PathVariable("id") Long id) {  // 수정 필요
        Project project = projectService.getProjectById(id);
        return new ProjectResponseDto(project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable("id") Long id) {  // 수정 필요
        projectService.deleteProject(id);
    }


    @PutMapping("/{id}")
    public ProjectResponseDto updateProject(@PathVariable("id") Long id, @RequestBody ProjectRequestDto dto)
    {
        Project updatedProject = projectService.updateProject(id, dto);
        return new ProjectResponseDto(updatedProject);
    }



}

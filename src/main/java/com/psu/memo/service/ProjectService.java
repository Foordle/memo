package com.psu.memo.service;

import com.psu.memo.dto.ProjectRequestDto;
import com.psu.memo.dto.TaskRequestDto;
import com.psu.memo.entity.Project;
import com.psu.memo.entity.Task;
import com.psu.memo.repository.ProjectRepository;
import com.psu.memo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByProjectName(String projectName) {
        return projectRepository.findByProjectNameContaining(projectName);
    }

    @Transactional
    public Project saveProject(ProjectRequestDto requestDto) {
        Project project = new Project();
        project.setProjectName(requestDto.getProjectName());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        project.setDescription(requestDto.getDescription());

        Project savedProject = projectRepository.save(project);

        if (requestDto.getTasks() != null && !requestDto.getTasks().isEmpty()) {
            List<Task> tasks = requestDto.getTasks().stream().map(taskDto -> {
                Task task = new Task();
                task.setTaskName(taskDto.getTaskName());
                task.setStartDate(taskDto.getStartDate());
                task.setEndDate(taskDto.getEndDate());
                task.setAssignedTo(taskDto.getAssignedTo());
                task.setProject(savedProject);
                return task;
            }).collect(Collectors.toList());

            taskRepository.saveAll(tasks);
        }
        return savedProject;
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. ID: " + id));
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. ID: " + id);
        }
        taskRepository.deleteByProjectId(id);
        projectRepository.deleteById(id);
    }


    @Transactional
    public Project updateProject(Long id, ProjectRequestDto requestDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. ID: " + id));

        project.setProjectName(requestDto.getProjectName());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        project.setDescription(requestDto.getDescription());

        // 기존 Task 삭제 후 새로 설정 (간단한 방식)
        taskRepository.deleteByProjectId(id);
        if (requestDto.getTasks() != null && !requestDto.getTasks().isEmpty()) {
            List<Task> tasks = requestDto.getTasks().stream().map(taskDto -> {
                Task task = new Task();
                task.setTaskName(taskDto.getTaskName());
                task.setStartDate(taskDto.getStartDate());
                task.setEndDate(taskDto.getEndDate());
                task.setAssignedTo(taskDto.getAssignedTo());
                task.setProject(project);
                return task;
            }).collect(Collectors.toList());
            taskRepository.saveAll(tasks);
        }

        return project;
    }
}

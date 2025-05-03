package com.psu.memo.service;

import com.psu.memo.dto.ProjectRequestDto;
import com.psu.memo.dto.TaskRequestDto;
import com.psu.memo.entity.Project;
import com.psu.memo.entity.Task;
import com.psu.memo.entity.TaskDependency;
import com.psu.memo.repository.ProjectRepository;
import com.psu.memo.repository.TaskRepository;
import com.psu.memo.repository.TaskDependencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskDependencyRepository taskDependencyRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, TaskDependencyRepository taskDependencyRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskDependencyRepository = taskDependencyRepository;
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

        List<Task> savedTasks = new ArrayList<>();
        if (requestDto.getTasks() != null && !requestDto.getTasks().isEmpty()) {
            List<Task> tasks = new ArrayList<>();

            for (TaskRequestDto taskDto : requestDto.getTasks()) {
                Task task = new Task();
                task.setTaskName(taskDto.getTaskName());
                task.setStartDate(taskDto.getStartDate());
                task.setEndDate(taskDto.getEndDate());
                task.setAssignedTo(taskDto.getAssignedTo());
                task.setProject(savedProject);
                tasks.add(task);
            }

            savedTasks = taskRepository.saveAll(tasks);

            // TaskDependency 저장
            for (int i = 0; i < requestDto.getTasks().size(); i++) {
                TaskRequestDto dto = requestDto.getTasks().get(i);
                Task target = savedTasks.get(i);
                if (dto.getPredecessorIds() != null && dto.getGaps() != null) {
                    for (int j = 0; j < dto.getPredecessorIds().size(); j++) {
                        Long preIdx = dto.getPredecessorIds().get(j);
                        int gap = dto.getGaps().get(j);
                        TaskDependency dep = new TaskDependency();
                        dep.setTask(target);
                        dep.setPredecessor(savedTasks.get(preIdx.intValue()));
                        dep.setGapDays(gap);
                        taskDependencyRepository.save(dep);
                    }
                }
            }
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

        // 기존 Task 및 의존성 삭제
        List<Task> oldTasks = taskRepository.findByProjectId(id);
        for (Task task : oldTasks) {
            taskDependencyRepository.deleteAll(task.getDependencies());
        }
        taskRepository.deleteAll(oldTasks);

        List<Task> savedTasks = new ArrayList<>();

        if (requestDto.getTasks() != null && !requestDto.getTasks().isEmpty()) {
            List<Task> tasks = new ArrayList<>();

            for (TaskRequestDto taskDto : requestDto.getTasks()) {
                Task task = new Task();
                task.setTaskName(taskDto.getTaskName());
                task.setAssignedTo(taskDto.getAssignedTo());
                task.setProject(project);

                LocalDate baseStartDate = taskDto.getStartDate();
                LocalDate resolvedStartDate = baseStartDate;

                long duration = ChronoUnit.DAYS.between(baseStartDate, taskDto.getEndDate());

                task.setStartDate(resolvedStartDate);
                task.setEndDate(resolvedStartDate.plusDays(duration));

                tasks.add(task);
            }

            savedTasks = taskRepository.saveAll(tasks);

            for (int i = 0; i < requestDto.getTasks().size(); i++) {
                TaskRequestDto dto = requestDto.getTasks().get(i);
                Task target = savedTasks.get(i);
                if (dto.getPredecessorIds() != null && dto.getGaps() != null) {
                    for (int j = 0; j < dto.getPredecessorIds().size(); j++) {
                        Long preIdx = dto.getPredecessorIds().get(j);
                        int gap = dto.getGaps().get(j);
                        TaskDependency dep = new TaskDependency();
                        dep.setTask(target);
                        dep.setPredecessor(savedTasks.get(preIdx.intValue()));
                        dep.setGapDays(gap);
                        taskDependencyRepository.save(dep);
                    }
                }
            }
        }
        return project;
    }
}

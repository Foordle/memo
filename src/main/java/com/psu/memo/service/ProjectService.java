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

    private List<Task> saveTasksWithDependencies(Project project, List<TaskRequestDto> taskDtos) {
        List<Task> savedTasks = new ArrayList<>();

        // 1. Task 먼저 저장
        List<Task> tasks = new ArrayList<>();
        for (TaskRequestDto dto : taskDtos) {
            Task task = new Task();
            task.setTaskName(dto.getTaskName());
            task.setAssignedTo(dto.getAssignedTo());
            task.setProject(project);

            LocalDate baseStartDate = dto.getStartDate();
            LocalDate resolvedStartDate = baseStartDate;

            // gap 기반 시작일 조정
            if (dto.getPredecessorIds() != null && dto.getGaps() != null &&
                    dto.getPredecessorIds().size() == dto.getGaps().size()) {

                for (int j = 0; j < dto.getPredecessorIds().size(); j++) {
                    Long preIndex = dto.getPredecessorIds().get(j);
                    int gap = dto.getGaps().get(j);
                    if (preIndex < savedTasks.size()) {
                        LocalDate candidateStart = savedTasks.get(preIndex.intValue()).getEndDate().plusDays(gap);
                        if (candidateStart.isAfter(resolvedStartDate)) {
                            resolvedStartDate = candidateStart;
                        }
                    }
                }
            }

            long duration = ChronoUnit.DAYS.between(baseStartDate, dto.getEndDate());
            task.setStartDate(resolvedStartDate);
            task.setEndDate(resolvedStartDate.plusDays(duration));

            tasks.add(task);
        }

        savedTasks = taskRepository.saveAll(tasks);

        // 2. 의존성 저장
        for (int i = 0; i < taskDtos.size(); i++) {
            TaskRequestDto dto = taskDtos.get(i);
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

        return savedTasks;
    }



    @Transactional
    public Project saveProject(ProjectRequestDto requestDto) {
        Project project = new Project();
        project.setProjectName(requestDto.getProjectName());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        project.setDescription(requestDto.getDescription());
        Project savedProject = projectRepository.save(project);

        if (requestDto.getTasks() != null) {
            saveTasksWithDependencies(savedProject, requestDto.getTasks());
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

        List<Task> oldTasks = taskRepository.findByProjectId(id);
        oldTasks.forEach(task -> taskDependencyRepository.deleteAll(task.getDependencies()));
        taskRepository.deleteAll(oldTasks);

        if (requestDto.getTasks() != null) {
            saveTasksWithDependencies(project, requestDto.getTasks());
        }

        return project;
    }

}

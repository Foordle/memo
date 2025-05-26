package com.psu.memo.dto;

import com.psu.memo.entity.Project;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProjectResponseDto {
    private Long id;
    private String projectName;
    private String startDate;
    private String endDate;
    private String description;
    private List<TaskResponseDto> tasks;


    public ProjectResponseDto(Project project) {
        this.id = project.getId();
        this.projectName = project.getProjectName();
        this.startDate = project.getStartDate().toString();
        this.endDate = project.getEndDate().toString();
        this.description = project.getDescription();

        // ✅ Task 리스트가 null일 경우 빈 리스트 반환하여 NPE 방지
        this.tasks = (project.getTasks() != null) ? project.getTasks().stream()
                .map(TaskResponseDto::new)
                .collect(Collectors.toList()) : List.of();
    }
}

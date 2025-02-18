package com.psu.memo.dto;

import com.psu.memo.entity.Project;
import lombok.Getter;

@Getter
public class ProjectResponseDto {
    private Long id;
    private String projectName;
    private String taskName;
    private String startDate;
    private String endDate;
    private String assignedTo;
    private String dependency;

    public ProjectResponseDto(Project project) {
        this.id = project.getId();
        this.projectName = project.getProjectName();
        this.taskName = project.getTaskName();
        this.startDate = project.getStartDate().toString();
        this.endDate = project.getEndDate().toString();
        this.assignedTo = project.getAssignedTo();
        this.dependency = project.getDependency();
    }
}

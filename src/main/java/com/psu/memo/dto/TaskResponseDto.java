package com.psu.memo.dto;

import com.psu.memo.entity.Task;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TaskResponseDto {
    private Long id;
    private String taskName;
    private String startDate;
    private String endDate;
    private String assignedTo;
    private List<Long> predecessorIds = new ArrayList<>();
    private List<Integer> gaps = new ArrayList<>();

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.taskName = task.getTaskName();
        this.startDate = task.getStartDate().toString();
        this.endDate = task.getEndDate().toString();
        this.assignedTo = task.getAssignedTo();

        if (task.getDependencies() != null) {
            for (var dep : task.getDependencies()) {
                predecessorIds.add(dep.getPredecessor().getId());
                gaps.add(dep.getGapDays());
            }
        }
    }
}

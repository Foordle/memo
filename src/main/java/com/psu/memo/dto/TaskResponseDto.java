package com.psu.memo.dto;

import com.psu.memo.entity.Task;  // ✅ 올바른 Task import
import lombok.Getter;

@Getter
public class TaskResponseDto {
    private Long id;
    private String taskName;
    private String startDate;
    private String endDate;
    private String assignedTo;

    public TaskResponseDto(Task task) {  // ✅ Task 객체를 받는 생성자 추가
        this.id = task.getId();
        this.taskName = task.getTaskName();
        this.startDate = task.getStartDate().toString();
        this.endDate = task.getEndDate().toString();
        this.assignedTo = task.getAssignedTo();
    }
}

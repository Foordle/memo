package com.psu.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequestDto {
    private String taskName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedTo;
}

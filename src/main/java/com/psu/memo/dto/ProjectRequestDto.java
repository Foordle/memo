package com.psu.memo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ProjectRequestDto {
    private String projectName;
    private String taskName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedTo;
    private String dependency;
}

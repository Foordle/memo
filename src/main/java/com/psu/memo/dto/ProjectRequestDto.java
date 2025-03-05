package com.psu.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectRequestDto {
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;


    private List<TaskRequestDto> tasks;
}

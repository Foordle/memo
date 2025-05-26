package com.psu.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskRequestDto {
    private String taskName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedTo;

    private List<Long> predecessorIds;  // 선행 작업 ID 목록 (index 기준)
    private List<Integer> gaps;         // 각 선행 작업과의 gap 일수
}

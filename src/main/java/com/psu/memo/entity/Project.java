package com.psu.memo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String taskName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedTo;  // 담당자
    private String dependency;  // 의존성 정보
}
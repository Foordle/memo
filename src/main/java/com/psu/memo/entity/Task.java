package com.psu.memo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedTo;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}

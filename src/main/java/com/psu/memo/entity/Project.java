package com.psu.memo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "project_name", nullable = false)
    private String projectName = "";

    @Column(name = "startDate", nullable = false, columnDefinition = "DATE DEFAULT CURDATE()")
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    // ✅ 프로젝트가 여러 개의 Task를 포함하도록 수정
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
}

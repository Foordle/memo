package com.psu.memo.repository;

import com.psu.memo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {  // 수정됨
    List<Project> findByProjectName(String projectName);
}

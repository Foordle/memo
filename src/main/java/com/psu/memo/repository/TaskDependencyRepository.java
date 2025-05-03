package com.psu.memo.repository;

import com.psu.memo.entity.TaskDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {
    // 필요 시 사용자 정의 메서드 추가 가능
}

package com.psu.memo.repository;

import com.psu.memo.entity.BookStudyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookStudyPostRepository extends JpaRepository<BookStudyPost, Long> {
    // 페이지 번호 기준으로 오름차순, 동일 페이지 번호일 경우 createdAt 기준으로 오름차순 정렬
    List<BookStudyPost> findAllByOrderByPageNumberAscCreatedAtAsc();

    List<BookStudyPost> findByPageNumber(int pageNumber);
}

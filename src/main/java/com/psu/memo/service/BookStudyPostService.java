package com.psu.memo.service;

import com.psu.memo.entity.BookStudyPost;
import com.psu.memo.repository.BookStudyPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookStudyPostService {

    private final BookStudyPostRepository bookStudyPostRepository;

    @Autowired
    public BookStudyPostService(BookStudyPostRepository bookStudyPostRepository) {
        this.bookStudyPostRepository = bookStudyPostRepository;
    }

    // 게시글 저장 기능
    public BookStudyPost savePost(BookStudyPost post) {
        return bookStudyPostRepository.save(post);
    }

    // 게시글 목록 조회 기능 (페이지 번호 오름차순, 동일 페이지 번호일 경우 작성 날짜 기준 오름차순으로 정렬)
    public List<BookStudyPost> getAllPosts() {
        return bookStudyPostRepository.findAllByOrderByPageNumberAscCreatedAtAsc();
    }

    public BookStudyPost getPostById(Long id) {
        return bookStudyPostRepository.findById(id).orElse(null);
    }

    public void deletePost(Long id) {
        bookStudyPostRepository.deleteById(id);
    }

    public List<BookStudyPost> findByPageNumber(int pageNumber) {
        return bookStudyPostRepository.findByPageNumber(pageNumber);
    }
}

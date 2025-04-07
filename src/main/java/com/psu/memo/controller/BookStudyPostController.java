package com.psu.memo.controller;

import com.psu.memo.entity.BookStudyPost;
import com.psu.memo.service.BookStudyPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/bookstudy")
public class BookStudyPostController {
    private final BookStudyPostService bookStudyPostService;

    @Autowired
    public BookStudyPostController(BookStudyPostService bookStudyPostService) {
        this.bookStudyPostService = bookStudyPostService;
    }

    // @PostMapping 수정하기
    @PostMapping
    public ResponseEntity<BookStudyPost> createPost(@RequestParam("pageNumber") int pageNumber,
                                                    @RequestParam("contents") String contents,
                                                    @RequestParam("image") MultipartFile image) {

        String imagePath = saveImage(image);

        if (imagePath == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        BookStudyPost post = new BookStudyPost();
        post.setPageNumber(pageNumber);
        post.setContents(contents);
        post.setImagePath(imagePath);

        BookStudyPost savedPost = bookStudyPostService.savePost(post);
        return ResponseEntity.ok(savedPost);
    }



    // ✅ 전체 게시글 목록 조회 API 추가
    @GetMapping
    public ResponseEntity<List<BookStudyPost>> getAllPosts() {
        List<BookStudyPost> posts = bookStudyPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookStudyPost> getPostById(@PathVariable("id") Long id) {  // 경로 변수 이름 지정
        BookStudyPost post = bookStudyPostService.getPostById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(post);
    }


    // ✅ saveImage 메서드 추가
    private String saveImage(MultipartFile image) {
        String folderPath = System.getProperty("user.dir") + "/src/main/resources/static/Bookimg/";
        File folder = new File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String originalFilename = image.getOriginalFilename();
        String filePath = folderPath + originalFilename;

        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // URL 경로 반환
        return "/Bookimg/" + originalFilename;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        try {
            bookStudyPostService.deletePost(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

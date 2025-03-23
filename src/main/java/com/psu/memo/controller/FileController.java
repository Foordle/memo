package com.psu.memo.controller;

import com.psu.memo.entity.FileEntity;
import com.psu.memo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    //upload
    @PostMapping("/upload")
    public ResponseEntity<FileEntity> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileEntity uploadFile = fileService.uploadFile(file);
            return ResponseEntity.ok(uploadFile);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //download
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {

        System.out.println("파일 다운로드 요청 ID: " + id);


        try {
            FileEntity fileEntity = fileService.getFileById(id);
            byte[] fileData = fileService.downloadFile(id);


            if (fileData != null) {

                String originalFileName = fileEntity.getOriginalFileName();
                String fileType = fileEntity.getFileType();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//                headers.setContentType(MediaType.parseMediaType(fileType));
//                String encodedFileName = java.net.URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20") ;
//                파일 이름에 한글 들어가면 다운로드 이상하게 되는데 아마 여기 고치면 될 수도


                headers.setContentDispositionFormData("attachment", originalFileName);



                return new ResponseEntity<>(fileData, headers, org.springframework.http.HttpStatus.OK);
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FileEntity>> getAllFiles() {
        List<FileEntity> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }


    //delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        boolean deleted = fileService.deleteFile(id);
        if (deleted)  {
            return ResponseEntity.ok("파일을 삭제했습니다.");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일을 찾을 수 없습니다.");
        }
    }

}

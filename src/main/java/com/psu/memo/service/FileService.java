package com.psu.memo.service;

import com.psu.memo.entity.FileEntity;
import com.psu.memo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    //properties 가서 바꿔야 되는데 아직 파일 저장 어디에 할지 확정이 안나서 일단 보류
    @Value("${file.upload-dir}")
    private String uploadDir;

//    @Value("${file.download-dir}")
//    private String downloadDir;

    //upload
    public FileEntity uploadFile(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        String filePath = uploadDir + java.io.File.separator + storedFilename;

        File dest = new File(filePath);
        file.transferTo(dest);

        FileEntity fileEntity = FileEntity.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFilename)
                .fileType(fileType)
                .fileSize(fileSize)
                .filePath(filePath)
                .build();

        return fileRepository.save(fileEntity);
    }


    //download
    public byte[] downloadFile(Long fileId) throws IOException{
        Optional<FileEntity> fileEntity = fileRepository.findById(fileId);
        if (fileEntity.isPresent()) {
            return Files.readAllBytes(Paths.get(fileEntity.get().getFilePath()));
        }

        return null;
    }

    public List<FileEntity> getAllFiles() {
        List<FileEntity> files = fileRepository.findAll();
        return files.stream()
                .filter(file -> new File(file.getFilePath()).exists())
                .collect(Collectors.toList());
    }


    public FileEntity getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("파일을 찾을 수 없습니다."));
    }


    //delete 인데 확인 안 해봄 나중에 필요하면 수정
    public boolean deleteFile(Long fileId) {
        Optional<FileEntity> fileEntity = fileRepository.findById(fileId);
        if(fileEntity.isPresent()) {
            File file = new File(fileEntity.get().getFileType());
            if(file.exists() &&  file.delete()) {
                fileRepository.deleteById(fileId);
                return true;
            }

            fileRepository.deleteById(fileId);
            return true;

        }

        return false;
    }
}

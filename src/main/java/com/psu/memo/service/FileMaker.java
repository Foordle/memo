package com.psu.memo.service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMaker {
    public void generateAndSaveFile() {
        // 파일을 저장할 디렉토리 경로 (서버상의 원하는 경로로 설정)
        String directoryName = "./generatedFiles";
        // 생성할 파일 이름 및 파일에 쓸 내용 설정
        String fileName = "example.txt";
        String content = "이 파일은 스프링 부트 서버에서 생성되었습니다.";

        // 디렉토리와 파일 경로 생성
        Path dirPath = Paths.get(directoryName);
        Path filePath = dirPath.resolve(fileName);

        try {
            // 디렉토리가 존재하지 않으면 생성 (폴더 생성시 필요한 권한이 있어야 함)
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            // 파일에 내용을 쓰기 (UTF-8 인코딩으로 저장)
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
            System.out.println("파일 저장 완료: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

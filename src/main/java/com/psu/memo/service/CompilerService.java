package com.psu.memo.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class CompilerService {

    public Map<String, String> compileCode(String code, String lang) throws IOException, InterruptedException {
        if (code == null || code.isEmpty() || lang == null || lang.isEmpty()) {
            throw new IllegalArgumentException("Code and language are required.");
        }

        Path filePath = createFile(code, lang).toAbsolutePath();

        String dockerCommand;
        switch (lang.toLowerCase()) {
            case "c":
                // C: gcc로 컴파일 후 실행
                dockerCommand = "docker run --rm -v " + filePath + ":/source.c gcc sh -c \"gcc /source.c -o /output && /output\"";
                break;
            case "cpp":
                // C++: g++로 컴파일 후 실행
                dockerCommand = "docker run --rm -v " + filePath + ":/source.cpp gcc sh -c \"g++ /source.cpp -o /output && /output\"";
                break;
            case "java":
                // Java: javac로 컴파일 후 실행 (Main 클래스 기준)
                dockerCommand = "docker run --rm -v " + filePath + ":/Main.java openjdk sh -c \"javac /Main.java && java -cp / Main\"";
                break;
            case "python":
                // Python: 바로 실행
                dockerCommand = "docker run --rm -v " + filePath + ":/source.py python python3 /source.py";
                break;
            default:
                throw new IllegalArgumentException("Unsupported language: " + lang);
        }

        try{
            String[] cmd = {"/bin/sh", "-c", dockerCommand};
            Process process = Runtime.getRuntime().exec(cmd);

            String line;

            // 명령어 실행 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 에러 스트림 읽기 (컴파일 에러 처리)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            // 결과 반환
            Map<String, String> response = new HashMap<>();
            response.put("output", !errorOutput.isEmpty() ? errorOutput.toString() : output.toString());

            process.destroy();
            return response;
        } finally {
            Files.deleteIfExists(filePath);
        }
    }

    private Path createFile(String code, String lang) throws IOException{
        String tempdir = "./TempFiles";
        Path dirPath = Paths.get(tempdir);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // 디렉토리가 없으면 생성
        }

        String extension;
        switch (lang.toLowerCase()) {
            case "c":
                extension = ".c";
                break;
            case "cpp":
                extension = ".cpp";
                break;
            case "java":
                extension = ".java";
                break;
            case "python":
                extension = ".py";
                break;
            default:
                throw new IllegalArgumentException("Unsupported language: " + lang);
        }

        Path filePath = dirPath.resolve("source" + extension);
        Files.write(filePath, code.getBytes(StandardCharsets.UTF_8));

        return filePath;
    }
}

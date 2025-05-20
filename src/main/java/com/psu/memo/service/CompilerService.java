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
    private final String tempdir = "./TempFiles";

    public Map<String, String> compileCode(String code, String lang) throws IOException, InterruptedException {
        if (code == null || code.isEmpty() || lang == null || lang.isEmpty()) {
            throw new IllegalArgumentException("Code and language are required.");
        }

        Path filePath = createFile(code, lang);
        Process testProcess = Runtime.getRuntime().exec("echo hello world");
        testProcess.destroy();

        try{
            String dockerCommand = "docker run --rm -v " + filePath + ":/source.c gcc sh -c \"/source.c -o /output && /output\"";
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

            //System.out.println(line);
            process.destroy();
            return response;
        } finally {
            Files.deleteIfExists(filePath);
        }
    }

    private Path createFile(String code, String lang) throws IOException{
        Path dirPath = Paths.get(tempdir).toAbsolutePath();

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // 디렉토리가 없으면 생성
        }

        Path filePath = dirPath.resolve("source.c");
        Files.write(filePath, code.getBytes(StandardCharsets.UTF_8));

        return filePath;
    }
}

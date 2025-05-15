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
        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        try{
            String dockerCommand = "docker run --rm -v " + filePath + ":/source.c gcc gcc /source.c -o /output && cat /output";
            String[] cmd = {"/bin/sh", "-c", dockerCommand};
            process = runtime.exec(cmd);

            // 명령어 실행 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
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
            response.put("output", errorOutput.length() > 0 ? errorOutput.toString() : output.toString());

            return response;
        }
        finally{
            //process.waitFor();
            //process.destroy();
            Files.deleteIfExists(filePath);
        }
    }

    private Path createFile(String code, String lang) throws IOException{
        Path dirPath = Paths.get(tempdir);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // 디렉토리가 없으면 생성
        }

        Path filePath = dirPath.resolve("source.c");
        Files.write(filePath, code.getBytes(StandardCharsets.UTF_8));

        return filePath;
    }
}

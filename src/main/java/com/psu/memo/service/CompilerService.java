package com.psu.memo.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class CompilerService {
    private final String tempdir = "./TempFiles";

    public Map<String, String> compileCode(String code, String lang) throws IOException {
        if (code == null || code.isEmpty() || lang == null || lang.isEmpty()) {
            throw new IllegalArgumentException("Code and language are required.");
        }

        Path filePath = createFile(code, lang);

        try{
            String dockerCommand = "docker run --rm -v " + filePath + ":/source.c gcc gcc /source.c -o /output && cat /output";
            Process process = Runtime.getRuntime().exec(dockerCommand);

            Map<String, String> response = new HashMap<>();
            response.put("output", "success");

            return response;
        }
        finally{
            Files.deleteIfExists(filePath);
        }
    }

    private Path createFile(String code, String lang) throws IOException{
        Path dirPath = Paths.get(tempdir);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // 디렉토리가 없으면 생성
        }

        Path filePath = dirPath.resolve("source.c");
        Files.write(filePath, code.getBytes());

        return filePath;
    }
}

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

    public Map<String, String> compileCode(String code, String lang) throws IOException {
        if (code == null || code.isEmpty() || lang == null || lang.isEmpty()) {
            throw new IllegalArgumentException("Code and language are required.");
        }
        final String tempdir = "./TempFiles";


        Path dirPath = Paths.get(tempdir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // 디렉토리가 없으면 생성
        }

        Path filePath = dirPath.resolve("source.txt");
        Files.write(filePath, code.getBytes());

        Map<String, String> response = new HashMap<>();

        return response;
    }
}

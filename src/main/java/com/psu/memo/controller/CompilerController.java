package com.psu.memo.controller;

import com.psu.memo.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/compile")
public class CompilerController {

    @Autowired
    private CompilerService compilerService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> compileCode(
            @RequestBody Map<String, String> request
    ) {
        try {
            String code = request.get("code");
            String lang = request.get("language");

            Map<String, String> response = compilerService.compileCode(code, lang);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("output", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("output", "Internal server error occurred."));
        }
    }
}

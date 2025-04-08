package com.psu.memo.controller;

import com.psu.memo.service.FileMaker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompilerController {
    @PostMapping("/upload")
    public String upload(){
        FileMaker fileMaker = new FileMaker();
        fileMaker.generateAndSaveFile();
        return "";

    }
}

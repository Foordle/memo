package com.psu.memo.controller;

import com.psu.memo.service.FileMaker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compile")
public class CompilerController {
    @PostMapping("/create-file")
    public void upload(){
        FileMaker fileMaker = new FileMaker();
        fileMaker.generateAndSaveFile();
        return;
    }






}

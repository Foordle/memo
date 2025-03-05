package com.psu.memo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/check-login")
    public boolean checkLogin(HttpSession session) {
        // 세션에 사용자 정보가 있는지 확인
        return session.getAttribute("loggedin") != null;
    }
}

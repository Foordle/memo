package com.psu.memo.controller;

import com.psu.memo.entity.User;
import com.psu.memo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model){

        boolean isAuthenticated = userService.authenticate(username, password);

        if (isAuthenticated) {
            // 로그인 성공 시 메인 페이지로 이동
            return "redirect:/?success=true";
        } else {
            // 로그인 실패 시 에러 메시지와 함께 다시 로그인 페이지로 이동
            return "redirect:/?error=true";
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
        }
    }

}

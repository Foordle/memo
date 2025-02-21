package com.psu.memo.controller;

import com.psu.memo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}

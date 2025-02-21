package com.psu.memo.service;

import com.psu.memo.entity.User;
import com.psu.memo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 사용자 인증 메서드
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        // 사용자가 존재하고 비밀번호가 일치하면 인증 성공
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}



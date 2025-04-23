package com.example.springrider.domain.user.controller;

import com.example.springrider.domain.user.dto.LoginRequestDto;
import com.example.springrider.domain.user.dto.SignupRequestDto;
import com.example.springrider.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup") // 회원가입
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 created
    }

    @PostMapping("/login") // 로그인
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto requestDto, HttpSession session) {
        userService.login(requestDto, session);
        return ResponseEntity.ok().build(); // 200 ok
    }
}

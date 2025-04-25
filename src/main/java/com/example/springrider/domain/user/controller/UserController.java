package com.example.springrider.domain.user.controller;

import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.PasswordModifyRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup") // 회원가입
    public ApiResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return ApiResponse.created(userService.signup(requestDto));
    }

    @PostMapping("/login") // 로그인
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto,
        HttpSession session) {
        LoginResponseDto dto = userService.login(requestDto, session);
        return ApiResponse.ok(dto);
    }


    @DeleteMapping("/withdraw") // 회원탈퇴
    public ApiResponse<Void> withdrawUser(
        @Valid @RequestBody DeleteUserRequestDto requestDto,
        @SessionAttribute(name = "userId", required = false) Long userId,
        HttpSession session
    ) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.AUTH_EXCEPTION);
        }

        userService.withdraw(requestDto, userId, session);
        return ApiResponse.ok(null);
    }

    @PutMapping("/password") // 비밀번호 수정
    public ApiResponse<Void> modifyPassword(
        @Valid @RequestBody PasswordModifyRequestDto requestDto,
        @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.AUTH_EXCEPTION);
        }

        userService.modifyPassword(requestDto, userId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/logout") // 로그아웃
    public ApiResponse<Void> logout(HttpSession session) {
        userService.logout(session);
        return ApiResponse.ok(null);
    }

}
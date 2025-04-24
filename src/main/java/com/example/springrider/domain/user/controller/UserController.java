package com.example.springrider.domain.user.controller;

import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.dto.response.WithdrawResponseDto;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        User savedUser = userService.signup(requestDto);
        return ApiResponse.created(new SignupResponseDto(savedUser));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto,
        HttpSession session) {
        LoginResponseDto dto = userService.login(requestDto, session);
        return ApiResponse.ok(dto);
    }


    @PatchMapping("/withdraw")
    public ApiResponse<WithdrawResponseDto> withdraw(
        @Valid @RequestBody DeleteUserRequestDto requestDto) {
        userService.deleteUser(requestDto);
        return ApiResponse.ok(new WithdrawResponseDto("회원 탈퇴가 완료되었습니다."));
    }

}

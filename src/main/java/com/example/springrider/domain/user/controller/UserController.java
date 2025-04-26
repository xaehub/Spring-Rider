package com.example.springrider.domain.user.controller;

import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.PasswordModifyRequestDto;
import com.example.springrider.domain.user.dto.request.ProfileModifyRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    /**
     * 회원가입 요청 컨트롤러
     *
     * @param requestDto 회원가입 정보가 담긴 {@link SignupRequestDto}
     * @return 생성된 회원 정보가 담긴 {@link SignupResponseDto}
     */
    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return ApiResponse.created(userService.signup(requestDto));
    }

    /**
     * 로그인 요청 컨트롤러
     *
     * @param requestDto 회원가입 정보가 담긴 {@link LoginRequestDto}
     * @param session    세션 정보
     * @return 로그인 정보가 담긴 {@link LoginResponseDto}
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(
        @Valid @RequestBody LoginRequestDto requestDto, HttpSession session
    ) {
        return ApiResponse.ok(userService.login(requestDto, session));
    }

    /**
     * 회원 탈퇴 요청 컨트롤러
     *
     * @param requestDto 회원 탈퇴 요청 정보가 담긴 {@link DeleteUserRequestDto}
     * @param userId     세션에 담긴 유저 식별자
     * @param session    세션 정보
     * @return 200 ok
     */
    @DeleteMapping("/withdraw") // 회원탈퇴
    public ApiResponse<Void> delete(
        @Valid @RequestBody DeleteUserRequestDto requestDto,
        @SessionAttribute(name = "userId", required = false) Long userId,
        HttpSession session
    ) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }
        userService.delete(requestDto, userId, session);
        return ApiResponse.ok(null);
    }

    /**
     * 비밀번호 수정 요청 컨트롤러
     *
     * @param requestDto 수정할 비밀번호 정보가 담긴 {@link PasswordModifyRequestDto}
     * @param userId     유저 식별자
     * @return 200 ok
     */
    @PutMapping("/password") // 비밀번호 수정
    public ApiResponse<Void> modifyPassword(
        @Valid @RequestBody PasswordModifyRequestDto requestDto,
        @SessionAttribute(name = "userId", required = false) Long userId,
        HttpSession session
    ) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }
        userService.modifyPassword(requestDto, userId, session);
        return ApiResponse.ok(null);
    }

    /**
     * 로그아웃 요청 컨트롤러
     *
     * @param session 세션 정보
     * @return 200 ok
     */
    @DeleteMapping("/logout") // 로그아웃
    public ApiResponse<Void> logout(HttpSession session) {
        userService.logout(session);
        return ApiResponse.ok(null);
    }

    @PatchMapping("/profile")
    public ApiResponse<Void> updateProfile(
        @Valid @RequestBody ProfileModifyRequestDto requestDto,
        @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        // 로그인 여부 확인
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }

        userService.updateProfile(requestDto, userId);

        return ApiResponse.ok(null);

    }
}
package com.example.springrider.domain.user.service;

import com.example.springrider.config.PasswordEncoder;
import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.PasswordModifyRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new InvalidRequestException(ExceptionCode.EMAIL_ALREADY_USED);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(
            requestDto.getEmail(),
            encodedPassword,
            requestDto.getName(),
            requestDto.getNickname(),
            requestDto.getPhone(),
            requestDto.getRole(),
            false,
            0
        );

        User savedUser = userRepository.save(user);
        return new SignupResponseDto(savedUser);
    }


    // 로그인 처리
    public LoginResponseDto login(LoginRequestDto requestDto, HttpSession session) {

        if (session.getAttribute("userId") != null) {
            throw new AuthException(ExceptionCode.ALREADY_LOGGED_IN);
        }

        User user = userRepository.findByEmailOrElseThrow(requestDto.getEmail());

        if (user.getIsWithdraw()) {
            throw new AuthException(ExceptionCode.ALREADY_DELETED_USER);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        session.setAttribute("userId", user.getId());

        return new LoginResponseDto(user);
    }


    // 회원 탈퇴
    public void withdraw(DeleteUserRequestDto requestDto, Long userId, HttpSession session) {

        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthException(ExceptionCode.USER_NOT_FOUND));

        // 탈퇴 상태 체크
        if (user.getIsWithdraw()) {
            throw new AuthException(ExceptionCode.ALREADY_DELETED_USER);
        }

        // 이메일 일치 체크
        if (!user.getEmail().equals(requestDto.getEmail())) {
            throw new AuthException(ExceptionCode.EMAIL_NOT_FOUND);
        }

        // 비밀번호 일치 체크
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        user.withdraw(); // 소프트 딜리트

        userRepository.save(user);

        session.invalidate();
    }

    // 비밀번호 수정
    public void modifyPassword(PasswordModifyRequestDto requestDto, Long userId,
        HttpSession session) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.USER_EXCEPTION));

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH); // 401
        }

        String newEncodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(newEncodedPassword);
        userRepository.save(user);
        session.invalidate(); // 비밀번호 수정후 자동 로그아웃
    }

    // 로그 아웃
    public void logout(HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED); // 로그인 후 이용 필요
        }
        session.invalidate(); // 현재 로그인 세션 제거
    }

}


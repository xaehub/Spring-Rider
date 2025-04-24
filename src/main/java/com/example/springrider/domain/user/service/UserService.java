package com.example.springrider.domain.user.service;

import com.example.springrider.config.PasswordEncoder;
import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.user.dto.LoginRequestDto;
import com.example.springrider.domain.user.dto.SignupRequestDto;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) { // 이메일 중복체크
            throw new InvalidRequestException(ExceptionCode.EMAIL_ALREADY_USED);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword()); // 비밀번호 암호화 후 저장
        User user = new User(
            requestDto.getEmail(),
            encodedPassword,
            requestDto.getName(),
            requestDto.getNickname(),
            requestDto.getPhone(),
            requestDto.getRole(),
            false,  // isWithdraw
            0       // store_count 초기값
        );

        userRepository.save(user);
    }

    // 로그인 처리
    public void login(LoginRequestDto requestDto, HttpSession session) {
        User user = userRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        session.setAttribute("userId", user.getId());
    }
}

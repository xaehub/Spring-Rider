package com.example.springrider.domain.user.service;

import com.example.springrider.config.PasswordEncoder;
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
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
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
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute("userId", user.getId());
    }
}

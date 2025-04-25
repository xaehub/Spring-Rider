package com.example.springrider.domain.user.service;

import com.example.springrider.config.PasswordEncoder;
import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
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
        User user = userRepository.findByEmailOrElseThrow(requestDto.getEmail());

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        session.setAttribute("userId", user.getId());

        return new LoginResponseDto(user);
    }


    // 회원 탈퇴
    public void deleteUser(DeleteUserRequestDto requestDto) {
        User user = userRepository.findByEmailOrElseThrow(requestDto.getEmail());

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        userRepository.delete(user);
    }

}


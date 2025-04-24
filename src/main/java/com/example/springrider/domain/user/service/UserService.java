package com.example.springrider.domain.user.service;

import com.example.springrider.config.PasswordEncoder;
import com.example.springrider.domain.user.dto.LoginRequestDto;
import com.example.springrider.domain.user.dto.SignupRequestDto;
import com.example.springrider.domain.user.dto.UserResponseDto;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
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

        userRepository.save(user);

        return toDto(user);
    }

    // 로그인
    public UserResponseDto login(LoginRequestDto requestDto, HttpSession session) {
        User user = userRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute("userId", user.getId());
        return toDto(user);
    }

    // 로그아웃
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // Entity → DTO 변환
    private UserResponseDto toDto(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getNickname(),
            user.getPhone(),
            user.getRole(),
            user.getCreatedAt(),
            user.getModifiedAt()
        );
    }
}

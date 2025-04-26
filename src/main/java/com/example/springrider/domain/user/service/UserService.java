package com.example.springrider.domain.user.service;

import com.example.springrider.config.PasswordEncoder;
import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.PasswordModifyRequestDto;
import com.example.springrider.domain.user.dto.request.ProfileModifyRequestDto;
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

    /**
     * 회원가입 요청 서비스
     *
     * @param requestDto 유저 정보가 담긴 {@link SignupRequestDto}
     * @return 회원가입된 유저 정보가 담긴 {@link SignupResponseDto}
     */
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new InvalidRequestException(ExceptionCode.EMAIL_ALREADY_USED);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.of(requestDto, encodedPassword, false, 0);

        User savedUser = userRepository.save(user);

        return SignupResponseDto.of(savedUser);
    }

    /**
     * 로그인 요청 서비스
     *
     * @param requestDto 로그인 정보가 담긴 {@link LoginRequestDto}
     * @param session    세션 정보
     * @return 로그인 유저 정보가 담긴 {@link LoginResponseDto}
     */
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

        return LoginResponseDto.of(user);
    }

    /**
     * 회원 탈퇴 요청 서비스
     *
     * @param requestDto 회원 정보가 담긴 {@link DeleteUserRequestDto}
     * @param userId     유저 식별자
     * @param session    세션 정보
     */
    public void delete(DeleteUserRequestDto requestDto, Long userId, HttpSession session) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }
        User user = userRepository.findByIdOrElseThrow(userId);

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

    /**
     * 비밀번호 수정 요청 서비스
     *
     * @param requestDto 비밀번호 정보가 담긴 {@link PasswordModifyRequestDto}
     * @param userId     유저 식별자
     */
    public void modifyPassword(
        PasswordModifyRequestDto requestDto, Long userId, HttpSession session) {

        User user = userRepository.findByIdOrElseThrow(userId);

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH); // 401
        }

        String newEncodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(newEncodedPassword);
        userRepository.save(user);
        session.invalidate(); // 자동 로그아웃
    }

    /**
     * 로그아웃 요청 서비스
     *
     * @param session 세션 정보
     */
    public void logout(HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }

        session.invalidate(); // 현재 로그인 세션 제거
    }

    public void updateProfile(ProfileModifyRequestDto requestDto, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        //  닉네임 중복 확인
        if (userRepository.existsByNicknameAndIdNot(requestDto.getNickname(), userId)) {
            throw new InvalidRequestException(ExceptionCode.NICKNAME_ALREADY_USED);
        }

        // 수정
        user.modifyProfile(requestDto.getNickname(), requestDto.getPhone());

        // 저장
        userRepository.save(user);
    }

}

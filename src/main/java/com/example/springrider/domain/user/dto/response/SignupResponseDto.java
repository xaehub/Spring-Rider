package com.example.springrider.domain.user.dto.response;

import com.example.springrider.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SignupResponseDto {

    private final Long userId;
    private final String email;
    private final String username;
    private final String nickname;
    private final String phone;
    private final String role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public SignupResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getName();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getModifiedAt();
    }
}

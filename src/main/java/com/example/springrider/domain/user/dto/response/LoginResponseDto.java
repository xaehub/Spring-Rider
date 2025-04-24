package com.example.springrider.domain.user.dto.response;

import com.example.springrider.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {

    private final Long userId;
    private final String email;
    private final String username;
    private final String nickname;
    private final String phone;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String role;

    public LoginResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getName();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getModifiedAt();
        this.role = user.getRole().name();
    }
}


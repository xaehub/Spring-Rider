package com.example.springrider.domain.user.dto;

import com.example.springrider.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserResponseDto {

    private Long userId;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

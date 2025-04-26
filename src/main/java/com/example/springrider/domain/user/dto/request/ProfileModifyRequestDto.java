package com.example.springrider.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProfileModifyRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}

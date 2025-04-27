package com.example.springrider.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateOrderStatusRequestDto {

    @NotNull(message = "Status는 필수 값입니다.")
    private final String status;
}

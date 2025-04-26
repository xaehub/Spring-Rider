package com.example.springrider.domain.cart.dto;

import com.example.springrider.global.validation.NotAllNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@NotAllNull
@RequiredArgsConstructor
public class UpdateCartItemRequestDto {

    private final String status;

    private final Integer quantity;
}

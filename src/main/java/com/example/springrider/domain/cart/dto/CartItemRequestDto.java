package com.example.springrider.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartItemRequestDto {


    @NotNull
    private final Long menuId;

    @Min(1)
    private final Integer quantity;
}


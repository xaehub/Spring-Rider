package com.example.springrider.domain.cart.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SuccessItemDto {

    private final Long cartItemId;

    private final Long menuId;
}

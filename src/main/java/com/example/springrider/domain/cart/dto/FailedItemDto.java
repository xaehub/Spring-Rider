package com.example.springrider.domain.cart.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FailedItemDto {

    private final Long menuId;

    private final String errorName;

    private final String message;
}

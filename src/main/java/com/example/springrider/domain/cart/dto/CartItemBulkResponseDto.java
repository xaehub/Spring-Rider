package com.example.springrider.domain.cart.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CartItemBulkResponseDto {

    private final List<SuccessItemDto> successItemDtos;

    private final List<FailedItemDto> failedItemDtos;
}

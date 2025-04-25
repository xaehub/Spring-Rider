package com.example.springrider.domain.cart.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartItemSearchBulkResponseDto {

    private final List<CartItemSearchResponseDto> responseDtos;

    private final Long storeId;

    private final int totalPrice;

    public CartItemSearchBulkResponseDto(
        List<CartItemSearchResponseDto> responseDtos,
        Long storeId) {

        this.responseDtos = responseDtos;
        this.storeId = storeId;
        this.totalPrice = sumTotalprice(responseDtos);
    }

    public int sumTotalprice(List<CartItemSearchResponseDto> responseDtos) {
        int sum = responseDtos.stream().mapToInt(d -> d.getPrice() * d.getQuantity()
        ).sum();
        return sum;
    }
}

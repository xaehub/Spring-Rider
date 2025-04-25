package com.example.springrider.domain.cart.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindCartItemBulkResponseDto {

    private final List<FindCartItemResponseDto> responseDtos;

    private final Long storeId;

    private final int totalPrice;

    public FindCartItemBulkResponseDto(
        List<FindCartItemResponseDto> responseDtos,
        Long storeId) {

        this.responseDtos = responseDtos;
        this.storeId = storeId;
        this.totalPrice = sumTotalprice(responseDtos);
    }

    public int sumTotalprice(List<FindCartItemResponseDto> responseDtos) {
        int sum = responseDtos.stream().mapToInt(d -> d.getPrice() * d.getQuantity()
        ).sum();
        return sum;
    }
}

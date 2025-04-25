package com.example.springrider.domain.cart.dto;

import com.example.springrider.domain.common.customannotation.NotAllNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@NotAllNull
@RequiredArgsConstructor
public class UpdateCartItemRequestDto {

    private final String status;

    private final Integer quantity;

    public boolean isValidQuantity() {
        return quantity != null && quantity > 0;
    }

    public boolean hasStatus() {
        return status != null;
    }
}

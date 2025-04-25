package com.example.springrider.domain.cart.dto;

import com.example.springrider.domain.cart.entity.CartItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartItemUpdateResponseDto {

    private final String Status;

    private final Integer quantity;

    public static CartItemUpdateResponseDto toDto(CartItem cartItem) {
        return new CartItemUpdateResponseDto(
            cartItem.getStatus().toString(),
            cartItem.getQuantity()
        );
    }
}

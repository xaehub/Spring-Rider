package com.example.springrider.domain.cart.dto.response;

import com.example.springrider.domain.cart.entity.CartItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FindCartItemResponseDto {

    private final Long cartItemId;

    private final String MenuName;

    private final Integer price;

    private final Integer quantity;

    public FindCartItemResponseDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.MenuName = cartItem.getMenu().getName();
        this.price = cartItem.getMenu().getPrice();
        this.quantity = cartItem.getQuantity();
    }
}

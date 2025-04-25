package com.example.springrider.domain.cart.controller;

import com.example.springrider.domain.cart.dto.CartItemBulkRequestDto;
import com.example.springrider.domain.cart.dto.CartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.CartItemSearchBulkResponseDto;
import com.example.springrider.domain.cart.service.CartService;
import com.example.springrider.domain.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Valid
    @PostMapping("/cart-items")
    public ApiResponse<CartItemBulkResponseDto> addCartItems(
        @RequestBody CartItemBulkRequestDto requestDto,
        @SessionAttribute(name = "userId", required = false) Long userId) {

        CartItemBulkResponseDto cartItemBulkResponseDto = cartService.addCartItems(userId,
            requestDto);
        return ApiResponse.ok(cartItemBulkResponseDto);
    }

    @GetMapping("/cart-items")
    public ApiResponse<CartItemSearchBulkResponseDto> searchCartItems(
        @SessionAttribute(name = "userId", required = false) Long userId) {

        return ApiResponse.ok(cartService.searchCartItems(userId));
    }
}

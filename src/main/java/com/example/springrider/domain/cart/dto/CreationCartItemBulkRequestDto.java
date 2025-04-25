package com.example.springrider.domain.cart.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreationCartItemBulkRequestDto {

    @NotNull
    private final Long storeId;

    @NotEmpty
    private final List<CreationCartItemRequestDto> cartItems;
}

package com.example.springrider.domain.cart.enums;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import java.util.Arrays;

public enum CartItemStatus {
    SELECT, DESELECT;

    public static CartItemStatus from(String value) {
        return Arrays.stream(CartItemStatus.values())
            .filter(status -> status.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.INVALID_CARTITEM_STATUS));
    }
}

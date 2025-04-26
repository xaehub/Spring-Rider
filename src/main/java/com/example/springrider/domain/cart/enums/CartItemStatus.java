package com.example.springrider.domain.cart.enums;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import java.util.Arrays;

public enum CartItemStatus {
    SELECT, DESELECT;

    public static CartItemStatus from(String value) {
        return Arrays.stream(CartItemStatus.values())
            .filter(status -> status.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.NO_CARTITEM_STATUS));
    }
}

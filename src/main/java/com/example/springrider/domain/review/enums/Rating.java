package com.example.springrider.domain.review.enums;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import java.util.Arrays;

public enum Rating {
    ONE, TWO, THREE, FOUR, FIVE;

    public static Rating from(String value) {
        return Arrays.stream(values())
            .filter(status -> status.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.INVALID_RATING));
    }
}

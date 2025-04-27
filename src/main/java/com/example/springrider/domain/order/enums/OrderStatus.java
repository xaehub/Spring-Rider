package com.example.springrider.domain.order.enums;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import java.util.Arrays;

public enum OrderStatus {
    PENDING,            // 주문 수락 대기 중
    RECEIVED,           // 식당이 주문을 수락
    COOKING,            // 조리 중
    DELIVERING,          // 배달 중
    DELIVERED,          // 배달 완료 (고객 수령)
    RIVIEWED,          // 리뷰 작성 완료
    CANCELED;          // 주문 취소

    public static OrderStatus from(String value) {
        return Arrays.stream(values())
            .filter(status -> status.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.INVALID_ORDER_STATUS));
    }
}

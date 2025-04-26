package com.example.springrider.domain.order.dto;

import com.example.springrider.domain.order.entity.Order;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {

    private final Long orderId;
    private final String storeName;
    private final List<OrderItemDto> item;

    public static OrderResponseDto of(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getStore().getName(),
            order.getOrderItems().stream()
                .map(OrderItemDto::of)
                .toList()
        );
    }

}

package com.example.springrider.domain.order.dto;

import com.example.springrider.domain.order.entity.Order;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateOrderResponseDto {

    private final Long orderId;
    private final String storeName;
    private final List<OrderItemDto> item;

    public static CreateOrderResponseDto of(Order order) {
        return new CreateOrderResponseDto(
            order.getId(),
            order.getStore().getName(),
            order.getOrderItems().stream()
                .map(OrderItemDto::of)
                .toList()
        );
    }

}

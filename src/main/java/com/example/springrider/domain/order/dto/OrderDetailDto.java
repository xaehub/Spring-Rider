package com.example.springrider.domain.order.dto;

import com.example.springrider.domain.order.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderDetailDto {

    private final Long orderId;
    private final String deliveryAddress;
    private final Integer totalPrice;
    private final String status;
    private final LocalDateTime createdAt;
    private final List<OrderItemDto> orderItems;

    public static OrderDetailDto of(Order order) {
        return new OrderDetailDto(
            order.getId(),
            order.getDeliveryAddress(),
            order.getTotalPrice(),
            order.getStatus().name(),
            order.getCreatedAt(),
            order.getOrderItems().stream()
                .map(OrderItemDto::of)
                .toList()
        );
    }
}
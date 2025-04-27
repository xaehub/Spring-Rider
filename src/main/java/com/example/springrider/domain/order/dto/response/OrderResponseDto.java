package com.example.springrider.domain.order.dto.response;

import com.example.springrider.domain.order.dto.OrderItemDto;
import com.example.springrider.domain.order.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {

    private final Long orderId;
    private final String storeName;
    private final String deliveryAddress;
    private final Integer totalPrice;
    private final String status;
    private final LocalDateTime createdAt;
    private final List<OrderItemDto> orderItems;

    public static OrderResponseDto of(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getStore().getName(),
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
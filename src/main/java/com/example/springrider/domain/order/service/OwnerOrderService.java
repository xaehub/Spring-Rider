package com.example.springrider.domain.order.service;

import com.example.springrider.aop.StoreOwnerCheck;
import com.example.springrider.domain.order.dto.UpdateOrderStatusRequestDto;
import com.example.springrider.domain.order.dto.UpdateOrderStatusResponseDto;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderRepository orderRepository;

    @Transactional
    @StoreOwnerCheck(userIdParam = "userId", storeIdParam = "storeId")
    public UpdateOrderStatusResponseDto update(
        Long orderId, Long storeId, Long userId,
        @Valid UpdateOrderStatusRequestDto requestDto
    ) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.changeStatus(OrderStatus.from(requestDto.getStatus()));
        return UpdateOrderStatusResponseDto.toDto(order);
    }

}

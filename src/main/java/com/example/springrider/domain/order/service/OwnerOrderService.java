package com.example.springrider.domain.order.service;

import com.example.springrider.aop.StoreOwnerCheck;
import com.example.springrider.domain.order.dto.request.UpdateOrderStatusRequestDto;
import com.example.springrider.domain.order.dto.response.OwnerOrderResponseDto;
import com.example.springrider.domain.order.dto.response.UpdateOrderStatusResponseDto;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

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

    @Transactional(readOnly = true)
    @StoreOwnerCheck(userIdParam = "userId", storeIdParam = "storeId")
    public List<OwnerOrderResponseDto> findAll(Long storeId, Long userId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        List<Order> orders = orderRepository.findAllByStoreIdWithOrderItemsAndMenu(storeId);

        return List.of(OwnerOrderResponseDto.of(store, orders));
    }

}
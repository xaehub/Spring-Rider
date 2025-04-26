package com.example.springrider.domain.order.service;

import com.example.springrider.aop.OrderEventLog;
import com.example.springrider.domain.cart.entity.CartItem;
import com.example.springrider.domain.cart.repository.CartRepository;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.order.dto.CreateOrderRequestDto;
import com.example.springrider.domain.order.dto.OrderResponseDto;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.entity.OrderItem;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    /**
     * 주문 요청 서비스
     *
     * @param requestDto 배달 주소 정보가 담긴 {@link CreateOrderRequestDto}
     * @param userId     유저 식별자
     * @return 생성된 주문 정보가 담긴 {@link OrderResponseDto}
     */
    @OrderEventLog
    public OrderResponseDto create(CreateOrderRequestDto requestDto, Long userId) {
        // 1. 장바구니 메뉴 조회
        List<CartItem> cartItems = cartRepository.findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
            userId, LocalDateTime.now().minusDays(1)
        );

        if (cartItems.isEmpty()) {
            throw new InvalidRequestException(ExceptionCode.CART_NOT_FOUND_ALL);
        }

        // 2. 사용자와 가게 정보 조회
        User user = userRepository.findByIdOrElseThrow(userId);
        Store store = cartItems.get(0).getMenu().getStore();

        // 3. 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setStore(store);
        order.setDeliveryAddress(requestDto.getAddress());
        order.setStatus(OrderStatus.PENDING);

        // 4. 장바구니 아이템을 주문 아이템으로 변환
        for (CartItem cartItem : cartItems) {
            if (!cartItem.isActive()) {
                continue;
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenu(cartItem.getMenu());
            orderItem.setQuantity(cartItem.getQuantity());

            order.add(orderItem);
        }

        order.calculateTotalPrice();

        // 5. 주문 저장
        Order savedOrder = orderRepository.save(order);
        Order orderWithItems = orderRepository.findByIdWithOrderItemsAndMenu(savedOrder.getId())
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.ORDER_NOT_FOUND));

        return OrderResponseDto.of(orderWithItems);

    }

    /**
     * 주문 목록 조회 서비스
     *
     * @param userId 유저 식별자
     * @return 주문 목록 정보가 담긴 {@link OrderResponseDto}
     */
    public OrderResponseDto findAll(Long userId) {
        return OrderResponseDto.of(orderRepository.findByUserIdWithOrderItemsAndMenu(userId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.ORDER_NOT_FOUND)));
    }

    public Long findStoreIdFromOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .map(order -> order.getStore().getId())
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));
    }

}

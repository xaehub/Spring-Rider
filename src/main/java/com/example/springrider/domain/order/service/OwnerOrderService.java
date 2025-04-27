package com.example.springrider.domain.order.service;

import com.example.springrider.config.PasswordEncoder;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.order.dto.request.CancelOrderRequestDto;
import com.example.springrider.domain.order.dto.request.UpdateOrderStatusRequestDto;
import com.example.springrider.domain.order.dto.response.CancelOrderResponseDto;
import com.example.springrider.domain.order.dto.response.UpdateOrderStatusResponseDto;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderCancelReason;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UpdateOrderStatusResponseDto update(Long orderId,
        @Valid UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new InvalidRequestException(
                ExceptionCode.ORDER_NOT_FOUND));
        order.changeStatus(OrderStatus.from(requestDto.getStatus()));
        return UpdateOrderStatusResponseDto.toDto(order);
    }

    @Transactional
    public CancelOrderResponseDto delete(
        Long storeId, Long orderId,
        Long userId, @Valid CancelOrderRequestDto requestDto) {
        //비밀번호 일치 여부 검증
        User user = userRepository.findById(userId).orElseThrow();
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidRequestException(ExceptionCode.PASSWORD_NOT_MATCH);
        }
        //취소 사유가 other(기타)일 경우에는 취소 메세지는 필수 값 -> 이를 검증
        String cancelReason = requestDto.getCancelReason();
        String cancelMessage = requestDto.getCancelMessage();
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new InvalidRequestException(
                ExceptionCode.ORDER_NOT_FOUND));
        if ("other".equals(cancelReason.toLowerCase(Locale.ROOT))) {
            if (isMessageNullOrBlank(cancelMessage)) {
                throw new InvalidRequestException(
                    ExceptionCode.REQUIRED_ORDER_CANCEL_MESSAGE_FOR_OTHER);
            }
        }
        //update dirty checking
        order.changeStatus(OrderStatus.CANCELED);
        order.setOrderCancelReason(OrderCancelReason.from(cancelReason));
        order.setCancelMessage(cancelMessage);
        //dto 반환
        return CancelOrderResponseDto.toDto(order);
    }

    //메세지 null 여부 검증 메서드
    public boolean isMessageNullOrBlank(String message) {
        return message == null || message.isBlank();
    }
}

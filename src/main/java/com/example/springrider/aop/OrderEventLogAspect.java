package com.example.springrider.aop;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.order.dto.CreateOrderResponseDto;
import com.example.springrider.domain.order.repository.OrderRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventLogAspect {

    private final OrderRepository orderRepository;

    @Around("@annotation(OrderEventLog)")
    public Object logOrderCreate(ProceedingJoinPoint joinPoint) throws Throwable {

        LocalDateTime requestTime = LocalDateTime.now();
        Object result = joinPoint.proceed();

        if (result instanceof CreateOrderResponseDto responseDto) {
            Long orderId = responseDto.getOrderId();
            Long storeId = orderRepository.findById(orderId)
                .map(order -> order.getStore().getId())
                .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));

            log.info("[주문 생성] 시간={}, 가게번호={}, 주문번호={}", requestTime, storeId, orderId);
        }

        return result;
    }

}

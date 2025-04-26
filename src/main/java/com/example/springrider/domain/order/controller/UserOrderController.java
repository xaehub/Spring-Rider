package com.example.springrider.domain.order.controller;

import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.order.dto.CreateOrderRequestDto;
import com.example.springrider.domain.order.dto.OrderResponseDto;
import com.example.springrider.domain.order.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/customers/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final UserOrderService userOrderService;

    /**
     * 주문 생성 요청 컨트롤러
     *
     * @param requestDto 주문 정보가 담긴 {@link CreateOrderRequestDto}
     * @return 생성된 주문 정보가 담긴 {@link OrderResponseDto}
     */
    @PostMapping
    public ApiResponse<OrderResponseDto> create(
        @RequestBody CreateOrderRequestDto requestDto,
        @SessionAttribute(name = "userId") Long userId
    ) {
        return ApiResponse.created(userOrderService.create(requestDto, userId));
    }

}

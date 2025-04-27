package com.example.springrider.domain.review.service;

import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.review.dto.CreateReviewRequestDto;
import com.example.springrider.domain.review.dto.ReviewResponseDto;
import com.example.springrider.domain.review.entity.Review;
import com.example.springrider.domain.review.repository.ReviewRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewResponseDto create(Long orderId, Long userId,
        @Valid CreateReviewRequestDto requestDto) {
        Order order = orderRepository.findByIdWithUser(orderId).orElseThrow();
        User findedUser = order.getUser();
        //타인의 주문에 대한 리뷰 생성을 요청한 경우
        if (!findedUser.getId().equals(userId)) {
            throw new InvalidRequestException(ExceptionCode.FORBIDDEN_REQUEST);
        }
        //주문 상태가 배달완료가 아닌 경우
        if (OrderStatus.DELIVERED != order.getStatus()) {
            throw new InvalidRequestException(ExceptionCode.REVIEW_NOT_AVAILABLE_YET);
        }
        //이미 이 주문에 대한 리뷰가 작성된 경우
        if (reviewRepository.existsByOrderId(order.getId())) {
            throw new InvalidRequestException(ExceptionCode.REVIEW_ALREADY_EXISTS);
        }
        order.changeStatus(OrderStatus.REVIEWED);
        Review review = Review.of(requestDto, findedUser, order);
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }
}

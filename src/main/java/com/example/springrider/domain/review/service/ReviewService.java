package com.example.springrider.domain.review.service;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.review.dto.CreateReviewRequestDto;
import com.example.springrider.domain.review.dto.ReviewResponseDto;
import com.example.springrider.domain.review.entity.Review;
import com.example.springrider.domain.review.repository.ReviewRepository;
import com.example.springrider.domain.user.entity.User;
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
        if (!findedUser.getId().equals(userId)) {
            throw new InvalidRequestException(ExceptionCode.FORBIDDEN_REQUEST);
        }
        Review review = Review.of(requestDto, findedUser, order);
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }
}

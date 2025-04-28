package com.example.springrider.domain.review.controller;


import com.example.springrider.domain.review.dto.request.CreateReviewRequestDto;
import com.example.springrider.domain.review.dto.response.FindAllReviewResponseDto;
import com.example.springrider.domain.review.dto.response.ReviewResponseDto;
import com.example.springrider.domain.review.service.ReviewService;
import com.example.springrider.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/customers/orders/{orderId}/reviews")
    public ApiResponse<ReviewResponseDto> create(
        @PathVariable Long orderId, @Valid @RequestBody CreateReviewRequestDto requestDto,
        @SessionAttribute(name = "userId") Long userId) {
        return ApiResponse.ok(reviewService.create(orderId, userId, requestDto));
    }

    @GetMapping("/customers/stores/{storeId}/reviews")
    public ApiResponse<FindAllReviewResponseDto> findAll(
        @PathVariable Long storeId) {
        return ApiResponse.ok(reviewService.findAll(storeId));
    }
}

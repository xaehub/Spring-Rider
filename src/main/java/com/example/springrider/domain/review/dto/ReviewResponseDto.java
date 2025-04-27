package com.example.springrider.domain.review.dto;

import com.example.springrider.domain.review.entity.Review;
import com.example.springrider.domain.review.enums.Rating;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewResponseDto {

    private final Long orderId;

    private final String contents;

    private final Rating rating;

    public static ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
            review.getOrder().getId(), review.getContents(), review.getRating()
        );
    }
}

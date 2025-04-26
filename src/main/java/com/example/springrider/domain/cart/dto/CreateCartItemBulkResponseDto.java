package com.example.springrider.domain.cart.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateCartItemBulkResponseDto {

    private final List<CreateSuccessDto> createSuccessDtos;

    private final List<CreateFailedDto> createFailedDtos;
}

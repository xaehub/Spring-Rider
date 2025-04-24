package com.example.springrider.domain.menu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuSaveRequestDto {

    private final String name;
    private final Integer price;
    private final String contents;
    private final String category;

}

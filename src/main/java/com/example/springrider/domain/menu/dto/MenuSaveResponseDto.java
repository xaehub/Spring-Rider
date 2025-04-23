package com.example.springrider.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuSaveResponseDto {

    private final String name;
    private final Integer price;
    private final String contents;

}

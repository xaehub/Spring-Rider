package com.example.springrider.domain.store.dto.response;

import com.example.springrider.domain.menu.dto.response.MenuResponseDto;
import com.example.springrider.domain.store.entity.Store;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindStoreResponseDto {

    private Long id;
    private String name;
    private String address;
    private String category;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minOrderPrice;
    private List<MenuResponseDto> menus;

    public static FindStoreResponseDto from(Store store) {
        List<MenuResponseDto> menuDtos = store.getMenus().stream()
            .map(MenuResponseDto::toDto)
            .collect(Collectors.toList());

        return new FindStoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getCategory(),
            store.getOpenTime(),
            store.getCloseTime(),
            store.getMinOrderPrice(),
            menuDtos
        );
    }
}
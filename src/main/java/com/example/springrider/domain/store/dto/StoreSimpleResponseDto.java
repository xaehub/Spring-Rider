package com.example.springrider.domain.store.dto;

import com.example.springrider.domain.store.entity.Store;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreSimpleResponseDto {

    private Long id;
    private String name;
    private String address;
    private String category;
    private Integer minOrderPrice;
    private LocalTime openTime;
    private LocalTime closeTime;

    public StoreSimpleResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.category = store.getCategory();
        this.minOrderPrice = store.getMinOrderPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
    }
}


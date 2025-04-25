package com.example.springrider.domain.store.dto;

import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponseDto {

    private Long id;
    private String name;
    private String address;
    private String category;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minOrderPrice;
    private StoreStatus status;
    private String ownerName;

    public static StoreResponseDto fromEntity(Store store) {
        return new StoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getCategory(),
            store.getOpenTime(),
            store.getCloseTime(),
            store.getMinOrderPrice(),
            store.getStatus(),
            store.getUser().getName()  // 여기서 바로 유저 이름 꺼내옴
        );
    }
}

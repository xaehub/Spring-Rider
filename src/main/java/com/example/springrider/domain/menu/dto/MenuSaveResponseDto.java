package com.example.springrider.domain.menu.dto;

import com.example.springrider.domain.menu.entity.Menu;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuSaveResponseDto {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String contents;
    private final String category;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static MenuSaveResponseDto toDto(Menu menu){
        return new MenuSaveResponseDto(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getContents(),
            menu.getCategory(),
            menu.getCreatedAt(),
            menu.getModifiedAt()
        );
    }

}

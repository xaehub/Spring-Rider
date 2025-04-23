package com.example.springrider.domain.menu.controller;

import com.example.springrider.domain.menu.dto.MenuSaveRequestDto;
import com.example.springrider.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owners/stores")
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 등록 컨트롤러
     *
     * @param storeId 등록할 메뉴의 가게 아이디
     * @return 등록된 메뉴 정보
     */
    @PostMapping("/{storeId}/menus")
    public ResponseEntity<Object> save(
        @PathVariable Long storeId,
        @RequestBody MenuSaveRequestDto requestDto
    ) {
        menuService.save(storeId, requestDto);
        return ResponseEntity.ok().build();
    }

}

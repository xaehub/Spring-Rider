package com.example.springrider.domain.menu.controller;

import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.menu.dto.MenuSaveRequestDto;
import com.example.springrider.domain.menu.dto.MenuSaveResponseDto;
import com.example.springrider.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
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
     * @param storeId 메뉴를 등록할 가게
     * @return {@link ApiResponse} nothing to return
     */
    @PostMapping("/{storeId}/menus")
    public ApiResponse<MenuSaveResponseDto> save(
        @PathVariable Long storeId,
        @RequestBody MenuSaveRequestDto requestDto
    ) {
        return ApiResponse.ok(menuService.save(storeId, requestDto));
    }

}

package com.example.springrider.domain.menu.controller;

import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.menu.dto.MenuRequestDto;
import com.example.springrider.domain.menu.dto.MenuResponseDto;
import com.example.springrider.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

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
    public ApiResponse<MenuResponseDto> save(
        @PathVariable Long storeId,
        @RequestBody MenuRequestDto requestDto,
        @SessionAttribute(name = "userId") Long userId
    ) {
        return ApiResponse.created(menuService.save(userId, storeId, requestDto));
    }


    @PatchMapping("/{storeId}/menus/{menuId}")
    public ApiResponse<MenuResponseDto> update(
        @PathVariable Long storeId, @PathVariable Long menuId,
        @RequestBody MenuRequestDto requestDto,
        @SessionAttribute Long userId
    ) {
        return ApiResponse.ok(menuService.update(storeId, menuId, requestDto));
    }


}

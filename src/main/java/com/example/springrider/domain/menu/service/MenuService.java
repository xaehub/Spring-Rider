package com.example.springrider.domain.menu.service;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.menu.dto.MenuRequestDto;
import com.example.springrider.domain.menu.dto.MenuResponseDto;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /**
     * 메뉴 등록 서비스
     *
     * @param storeId    메뉴를 등록할 가게
     * @param requestDto 메뉴 정보가 담긴 {@link MenuRequestDto}
     */
    public MenuResponseDto save(Long userId, Long storeId, MenuRequestDto requestDto) {
        Store findStore = findStore(storeId);
        Menu menu = new Menu(requestDto);
        menu.setStore(findStore);

        // 메뉴 저장
        menuRepository.save(menu);

        return MenuResponseDto.toDto(menu);
    }

    @Transactional
    public MenuResponseDto update(Long storeId, Long menuId, MenuRequestDto requestDto) {
        Menu findMenu = findMenu(menuId);
        findMenu.updateMenu(requestDto);

        return MenuResponseDto.toDto(findMenu);
    }

    private Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.MENU_NOT_FOUND));
    }

}

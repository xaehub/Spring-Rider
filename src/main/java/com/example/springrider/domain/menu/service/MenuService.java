package com.example.springrider.domain.menu.service;

import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.menu.dto.MenuRequestDto;
import com.example.springrider.domain.menu.dto.MenuResponseDto;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
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
        Store findStore = storeRepository.findByIdOrElseThrow(storeId);

        if (!Objects.equals(findStore.getUser().getId(), userId)) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }

        Menu menu = new Menu(requestDto);
        menu.setStore(findStore);

        // 메뉴 저장
        menuRepository.save(menu);

        return MenuResponseDto.toDto(menu);
    }

    @Transactional
    public MenuResponseDto update(Long storeId, Long menuId, MenuRequestDto requestDto) {
        Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);
        findMenu.updateMenu(requestDto);

        return MenuResponseDto.toDto(findMenu);
    }

}

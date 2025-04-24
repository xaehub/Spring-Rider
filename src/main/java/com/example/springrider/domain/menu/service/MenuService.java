package com.example.springrider.domain.menu.service;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.menu.dto.MenuSaveRequestDto;
import com.example.springrider.domain.menu.dto.MenuSaveResponseDto;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
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
     * @param requestDto 메뉴 정보가 담긴 {@link MenuSaveRequestDto}
     */
    public MenuSaveResponseDto save(Long storeId, MenuSaveRequestDto requestDto) {
        Store findStore = storeRepository.findById(storeId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));
        Menu menu = new Menu(requestDto);
        menu.setStore(findStore);

        // 메뉴 저장
        menuRepository.save(menu);

        return MenuSaveResponseDto.toDto(menu);
    }

}

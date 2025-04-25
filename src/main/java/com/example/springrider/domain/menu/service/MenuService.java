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

        if (!isStoreOwner(userId, storeId)) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }

        Menu menu = new Menu(requestDto);
        menu.setStore(findStore);

        // 메뉴 저장
        menuRepository.save(menu);

        return MenuResponseDto.toDto(menu);
    }

    /**
     * 메뉴 수정 서비스
     *
     * @param storeId    가게 아이디
     * @param menuId     메뉴 아이디
     * @param userId     유저 아이디 (인터셉터로 처리 예정)
     * @param requestDto 수정할 메뉴 정보가 담긴 {@link MenuRequestDto}
     * @return 수정된 메뉴 정보가 담긴 {@link MenuResponseDto}
     */
    @Transactional
    public MenuResponseDto update(
        Long storeId, Long menuId, Long userId,
        MenuRequestDto requestDto
    ) {
        if (!isStoreOwner(userId, storeId)) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }

        Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);
        findMenu.updateMenu(requestDto);

        return MenuResponseDto.toDto(findMenu);
    }

    /**
     * 메뉴 삭제 서비스
     *
     * @param storeId 가게 식별자
     * @param menuId  메뉴 식별자
     * @param userId  유저 식별자
     */
    @Transactional
    public MenuResponseDto delete(Long storeId, Long menuId, Long userId) {
        if (!isStoreOwner(userId, storeId)) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }
        Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);
        findMenu.deleteMenu();

        menuRepository.save(findMenu);

        return MenuResponseDto.toDto(findMenu);
    }

    /**
     * 가게의 주인인지 확인하는 메소드
     *
     * @param userId  유저 식별자
     * @param storeId 가게 식별자
     * @return 주인이면 true, 주인이 아니면 false
     */
    private boolean isStoreOwner(Long userId, Long storeId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        return Objects.equals(store.getUser().getId(), userId);
    }

}

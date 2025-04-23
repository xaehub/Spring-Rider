package com.example.springrider.domain.menu.service;

import com.example.springrider.domain.menu.dto.MenuSaveRequestDto;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
//    private final StoreRepository storeRepository;

    public void save(Long storeId, MenuSaveRequestDto requestDto) {

//        Store findStore = storeRepository.find(storeId);
        Menu menu = new Menu(requestDto);
//        menu.setStore(findStore);
        Menu saveMenu = menuRepository.save(menu);
    }

}

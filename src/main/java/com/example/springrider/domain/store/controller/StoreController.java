package com.example.springrider.domain.store.controller;

import com.example.springrider.domain.store.dto.StoreRequestDto;
import com.example.springrider.domain.store.dto.StoreResponseDto;
import com.example.springrider.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/owners/stores")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponseDto createStore(@Valid @RequestBody StoreRequestDto storeRequestDto) {
        return storeService.createStore(storeRequestDto);
    }
}

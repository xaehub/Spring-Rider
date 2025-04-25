package com.example.springrider.domain.store.controller;

import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.store.dto.StoreDetailResponseDto;
import com.example.springrider.domain.store.dto.StoreRequestDto;
import com.example.springrider.domain.store.dto.StoreResponseDto;
import com.example.springrider.domain.store.dto.StoreSimpleResponseDto;
import com.example.springrider.domain.store.dto.StoreUpdateRequestDto;
import com.example.springrider.domain.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    /**
     * 가게 생성 (사장만)
     *
     * @param storeRequestDto 가게 정보 요청 dto
     * @param userId          로그인 상태인 유저의 세션 정보
     * @return 가게 정보 + 유저 이름
     */
    @PostMapping("/owners/stores")
    public ApiResponse<StoreResponseDto> createStore(
        @Valid @RequestBody StoreRequestDto storeRequestDto,
        @SessionAttribute(name = "userId") Long userId
    ) {
        return ApiResponse.created(storeService.createStore(storeRequestDto, userId));
    }

    /**
     * 가게 수정 (사장만)
     *
     * @param storeId    수정할 가게의 고유 식별자
     * @param requestDto 수정할 정보가 담긴 dto
     * @param userId     로그인 상태인 유저의 세션 정보
     * @return 수정된 가게 정보 + 유저 정보
     */
    @PutMapping("/owners/stores/{storeId}")
    public ApiResponse<StoreResponseDto> updateStore(
        @Valid @RequestBody StoreUpdateRequestDto requestDto,
        @PathVariable Long storeId,
        @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        StoreResponseDto responseDto = storeService.updateStore(storeId, requestDto, userId);
        return ApiResponse.ok(responseDto);
    }

    /**
     * 모든 (ACTIVE)상태의 가게 목록 조회
     *
     * @return 오픈 상태의 가게를 리스트형태로 store에 담아 Map 형태로 반환함
     */
    @GetMapping("/customers/stores")
    public ApiResponse<Map<String, List<StoreSimpleResponseDto>>> getAllOpenStores() {
        List<StoreSimpleResponseDto> stores = storeService.getAllStores();
        // HTTP 응답 포맷
        Map<String, List<StoreSimpleResponseDto>> response = new HashMap<>();
        response.put("store", stores);

        return ApiResponse.ok(response);
    }

    /**
     * 가게 단건 조회
     *
     * @param storeId 조회할 가게 고유 식별자
     * @return 가게의 상세 정보
     */
    @GetMapping("/customers/stores/{storeId}")
    public ApiResponse<StoreDetailResponseDto> getStoreDetail(@PathVariable Long storeId) {
        StoreDetailResponseDto response = storeService.getStoreDetail(storeId);
        return ApiResponse.ok(response);
    }
}

package com.example.springrider.domain.store.service;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.store.dto.StoreRequestDto;
import com.example.springrider.domain.store.dto.StoreResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponseDto createStore(StoreRequestDto storeRequestDto) {

        User user = userRepository.findById(storeRequestDto.getUserId())
            .orElseThrow(
                () -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND)); // ← 유저 없을 때

        if (user.getRole() != UserRole.OWNER) {
            throw new InvalidRequestException(ExceptionCode.STORE_OWNER_ONLY); // ← 사장님 권한 없을 때
        }

        // 가게 수가 3개가 넘으면 예외 처리
        long storeCount = storeRepository.countByUser(user);
        if (storeCount >= 3) {
            throw new InvalidRequestException(ExceptionCode.STORE_LIMIT_EXCEEDED);
        }

        // 생성한 가게 정보를 가진 객체 생성
        Store store = new Store(
            storeRequestDto.getName(),
            storeRequestDto.getAddress(),
            storeRequestDto.getCategory(),
            storeRequestDto.getOpenTime(),
            storeRequestDto.getCloseTime(),
            storeRequestDto.getMinOrderPrice(),
            StoreStatus.ACTIVE,
            user
        );

        // 저장
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.fromEntity(savedStore);
    }
}

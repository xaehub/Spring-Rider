package com.example.springrider.domain.store.service;

import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_INVALID_STATUS_CHANGE;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_INVALID_TIME;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_LIMIT_EXCEEDED;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_NOT_FOUND;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_USER_MISMATCH;
import static com.example.springrider.domain.common.exception.ExceptionCode.USER_NOT_FOUND;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.store.dto.StoreRequestDto;
import com.example.springrider.domain.store.dto.StoreResponseDto;
import com.example.springrider.domain.store.dto.StoreSimpleResponseDto;
import com.example.springrider.domain.store.dto.StoreUpdateRequestDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponseDto createStore(StoreRequestDto storeRequestDto) {

        User user = userRepository.findById(storeRequestDto.getUserId())
            .orElseThrow(
                () -> new InvalidRequestException(STORE_NOT_FOUND)); // ← 유저 없을 때

        if (user.getRole() != UserRole.OWNER) {
            throw new InvalidRequestException(ExceptionCode.STORE_OWNER_ONLY); // ← 사장님 권한 없을 때
        }

        // 가게 수가 3개가 넘으면 예외 처리
        long storeCount = storeRepository.countByUser(user);
        if (storeCount >= 3) {
            throw new InvalidRequestException(STORE_LIMIT_EXCEEDED);
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

    /**
     * 가게 정보 수정
     *
     * @param storeId    가게 고유 식별자
     * @param requestDto 수정할 가게 정보가 담긴 dto
     * @param userId     유저의 세션 정보
     */
    @Transactional
    public void updateStore(Long storeId, StoreUpdateRequestDto requestDto, Long userId) {

        // userID가 null이면 USER_NOT_FOUND
        if (userId == null) {
            throw new InvalidRequestException(USER_NOT_FOUND);
        }

        // 해당 유저가 있는지 확인하는 코드
        User currentUser = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException(USER_NOT_FOUND));

        // 가게가 존재하는지 확인하는 코드
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new InvalidRequestException(STORE_NOT_FOUND));

        // 지금 로그인한 유저가 사장이 맞는지 확인
        if (!store.getUser().getId().equals(currentUser.getId())) {
            throw new InvalidRequestException(STORE_USER_MISMATCH);
        }

        // 상태 변경 로직
        StoreStatus newStatus = requestDto.getStatus();

        if (store.getStatus() != newStatus) {
            if (newStatus == StoreStatus.ACTIVE) {
                // 사장님이 가지고 있는 ACTIVE 상태의 가게 수가 3개가 넘으면 예외처리
                long activeStoreCount = storeRepository.countByUserAndStatus(currentUser,
                    StoreStatus.ACTIVE);
                if (activeStoreCount >= 3) {
                    throw new InvalidRequestException(STORE_LIMIT_EXCEEDED);
                }
                store.changeStatus(StoreStatus.ACTIVE);
            } else if (newStatus == StoreStatus.CLOSED) {
                // CLOSED 상태에서 또 CLOSED로 변경해도 괜찮음 (수정할 때 store status를 받아야 함)
                store.changeStatus(StoreStatus.CLOSED);
            } else {
                throw new InvalidRequestException(STORE_INVALID_STATUS_CHANGE);
            }
        }

        // 오픈 시간이 마감 시간보다 느릴 때
        if (requestDto.getOpenTime().isAfter(requestDto.getCloseTime())) {
            throw new InvalidRequestException(STORE_INVALID_TIME);
        }

        // 가게 정보 수정
        store.update(requestDto);
    }

    @Transactional(readOnly = true)
    public List<StoreSimpleResponseDto> getAllStores() {
        List<Store> stores = storeRepository.findAllByStatusNot(StoreStatus.CLOSED);
        return stores.stream()
            .map(StoreSimpleResponseDto::new)
            .toList();
    }
}

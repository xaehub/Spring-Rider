package com.example.springrider.domain.store.service;

import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_ALREADY_CLOSED;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_INVALID_STATUS_CHANGE;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_INVALID_TIME;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_LIMIT_EXCEEDED;
import static com.example.springrider.domain.common.exception.ExceptionCode.STORE_USER_MISMATCH;

import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.store.dto.StoreDetailResponseDto;
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

    /**
     * 가게 생성
     *
     * @param storeRequestDto 가게 생성 요청 dto
     * @param userId          로그인한 유저의 고유 식별자
     * @return 가게 정보 + 유저 이름을 savedStore에 저장
     */
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto storeRequestDto, Long userId) {

        User user = userRepository.findByIdOrElseThrow(userId);

        // 로그인한 유저의 롤이 오너가 아니면 예외 처리
        if (user.getRole() != UserRole.OWNER) {
            throw new InvalidRequestException(ExceptionCode.STORE_OWNER_ONLY);
        }

        // 가게 수가 3개가 넘으면 예외 처리
        long storeCount = storeRepository.countByUser(user);
        if (storeCount >= 3) {
            throw new InvalidRequestException(STORE_LIMIT_EXCEEDED);
        }

        // store에 있는 스태틱 메소드 StoreInfo에 가게정보와 유저의정보(유저이름)을 담고
        // 객체 store 생성
        Store store = Store.StoreInfo(storeRequestDto, user);

        // 저장
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.fromEntity(savedStore);
    }

    /**
     * 가게 수정
     *
     * @param storeId    가게 고유 식별자
     * @param requestDto 가게 수정 요청 dto
     * @param userId     유저 고유 식별자
     * @return 수정된 가게 정보
     */
    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreUpdateRequestDto requestDto,
        Long userId) {

        User user = userRepository.findByIdOrElseThrow(userId);

        // 가게가 존재하는지 확인하는 코드
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 지금 로그인한 유저가 사장이 맞는지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            throw new InvalidRequestException(STORE_USER_MISMATCH);
        }

        // 상태 변경 로직
        StoreStatus newStatus = requestDto.getStatus();

        // 현재 가게 상태와 입력받은 상태가 다를 때
        if (store.getStatus() != newStatus) {
            if (newStatus == StoreStatus.ACTIVE) {
                // 사장님이 가지고 있는 ACTIVE 상태의 가게 수가 3개가 넘으면 예외처리
                long activeStoreCount = storeRepository.countByUserAndStatus(user,
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

        return StoreResponseDto.fromEntity(store);
    }

    /**
     * 전체 가게 목록 조회 ACTIVE 상태인 가게만 노출
     *
     * @return 간단한 가게 정보 리스트
     */
    @Transactional(readOnly = true)
    public List<StoreSimpleResponseDto> getAllStores() {
        // 영업 중인 가게만 조회 (상태가 CLOSED가 아닌 가게만 조회)
        List<Store> stores = storeRepository.findAllByStatusNot(StoreStatus.CLOSED);

        // 리스트에 담은 stores를 리스트에 담긴 store simple response dto형태로 변환
        return stores.stream()
            .map(StoreSimpleResponseDto::new)
            .toList();
    }

    /**
     * 가게 상세 조회
     *
     * @param storeId 가게 고유 식별자
     * @return 가게 상세 정보를 담은 dto
     */
    public StoreDetailResponseDto getStoreDetail(Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        // CLOSED된 가게인 경우 예외 처리
        if (store.getStatus() == StoreStatus.CLOSED) {
            throw new InvalidRequestException(STORE_ALREADY_CLOSED);
        }

        // 가게의 정보와 메뉴도 보이게 가게 상세 dto 형태 반환
        return StoreDetailResponseDto.from(store);
    }
}

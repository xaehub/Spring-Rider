package com.example.springrider.domain.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.store.dto.FindStoresResponseDto;
import com.example.springrider.domain.store.dto.StoreRequestDto;
import com.example.springrider.domain.store.dto.StoreResponseDto;
import com.example.springrider.domain.store.dto.UpdateStoreRequestDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private User mockUser;
    private StoreRequestDto storeRequestDto;

    @BeforeEach
    void setUp() throws Exception {
        // 가짜 유저 mockUser를 생성함
        mockUser = new User(
            "owner@example.com",
            "encodedPassword",
            "김태정",
            "taejung",
            "01012345678",
            UserRole.OWNER,
            false,
            0
        );

        // User 클래스는 id를 직접 설정할 수 없으니, 리플렉션으로 강제로 설정 *
        Field idField = mockUser.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(mockUser, 1L);

        // 가게 생성 요청 dto 생성
        storeRequestDto = new StoreRequestDto(
            "김태정의 맛집",
            "서울특별시",
            "한식",
            LocalTime.of(9, 0),
            LocalTime.of(22, 0),
            10000,
            StoreStatus.ACTIVE,
            1L
        );
    }

    @AfterEach
    void afterEach() {
        System.out.println("테스트 코드 성공했습니동동");
    }

    @Test
    public void create_store_가게가_정상적으로_생성된다() {
        // given 가짜 레포지토리에서 mockuser를 찾고
        when(userRepository.findByIdOrElseThrow(anyLong())).thenReturn(mockUser);
        when(storeRepository.countByUser(any(User.class))).thenReturn(0L);

        Store savedStore = new Store();
        savedStore.setUser(mockUser);

        when(storeRepository.save(any(Store.class))).thenReturn(savedStore);

        // when
        StoreResponseDto responseDto = storeService.create(storeRequestDto, 1L);

        // then
        assertNotNull(responseDto);
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void create_store_가게_생성_시_3개가_초과하면_예외_처리() {

        // given 유저가 가게 3개를 가지고 있음
        when(userRepository.findByIdOrElseThrow(anyLong())).thenReturn(mockUser);
        when(storeRepository.countByUser(mockUser)).thenReturn(3L);

        StoreRequestDto requestDto = new StoreRequestDto(
            "맛집", "서울", "한식",
            LocalTime.of(10, 0),
            LocalTime.of(20, 0),
            10000,
            StoreStatus.ACTIVE,
            1L
        );

        //  when(storeService.create을 실행헀을 때) then*( 에러가 발생)
        assertThrows(InvalidRequestException.class, () -> {
            storeService.create(requestDto, 1L);
        });
    }

    @Test
    void update_store_가게가_정상적으로_수정된다() {

        // given 수정할 때 보내는 dto와 수정할 가게 준비
        UpdateStoreRequestDto dto = new UpdateStoreRequestDto(
            "레전드 맛집", "서울", "양식",
            LocalTime.of(9, 0), LocalTime.of(21, 0),
            15000, StoreStatus.CLOSED
        );

        Store store = Store.StoreInfo(new StoreRequestDto(
            "지리는 맛집", "서울", "한식",
            LocalTime.of(10, 0), LocalTime.of(20, 0),
            10000, StoreStatus.ACTIVE, 1L
        ), mockUser);

        when(userRepository.findByIdOrElseThrow(anyLong())).thenReturn(mockUser);
        when(storeRepository.findByIdOrElseThrow(anyLong())).thenReturn(store);

        // when
        StoreResponseDto result = storeService.update(1L, dto, 1L);

        // then
        assertEquals("레전드 맛집", result.getName());
    }

    @Test
    void update_store_다른_유저가_가게_정보_수정을_시도할_때_예외_처리() {
        // given 권한이 없는 다른 사용자와 가게 준비
        User anotherUser = new User("asd@email.com", "1111", "김태군", "김태정아님", "01000000000",
            UserRole.OWNER, false, 0);
        Field idField;
        try {
            idField = anotherUser.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(anotherUser, 2L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Store store = Store.StoreInfo(new StoreRequestDto(
            "레전드 맛집", "서울", "한식",
            LocalTime.of(10, 0), LocalTime.of(20, 0),
            10000, StoreStatus.ACTIVE, 1L
        ), mockUser);

        when(userRepository.findByIdOrElseThrow(2L)).thenReturn(anotherUser);
        when(storeRepository.findByIdOrElseThrow(1L)).thenReturn(store);

        UpdateStoreRequestDto dto = new UpdateStoreRequestDto(
            "전설적인 집", "서울", "한식",
            LocalTime.of(10, 0), LocalTime.of(22, 0),
            11000, StoreStatus.CLOSED
        );

        // when(storeService.update를 실행할 때) then(예외 처리)
        assertThrows(InvalidRequestException.class, () -> {
            storeService.update(1L, dto, 2L);
        });
    }

    @Test
    void finds_전체_가게_조회에_성공한다() {
        // given 가게 6개를 만들고 반환할 준비
        // 가게0, 가게1, 가게2, 가게3 ...
        List<Store> dummyStores = IntStream.range(0, 6)
            .mapToObj(i -> Store.StoreInfo(
                new StoreRequestDto("가게" + i, "서울", "한식",
                    LocalTime.of(9, 0), LocalTime.of(22, 0), 10000,
                    StoreStatus.ACTIVE, 1L
                ), mockUser))
            .collect(Collectors.toList());

        // store 레포지토리가 findAllByStatusNot(StoreStatus.CLOSED)를 호출하면 위에서 만든 가게 6개를 반환
        when(storeRepository.findAllByStatusNot(StoreStatus.CLOSED)).thenReturn(dummyStores);

        // when finds 메소드 호출
        List<FindStoresResponseDto> stores = storeService.finds();

        // then 그럼 stores리스트 크기가 6인지 홧ㄱ인
        assertEquals(6, stores.size());
    }
}

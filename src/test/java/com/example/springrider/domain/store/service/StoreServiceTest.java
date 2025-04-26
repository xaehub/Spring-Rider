package com.example.springrider.domain.store.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springrider.domain.store.dto.StoreRequestDto;
import com.example.springrider.domain.store.dto.StoreResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.time.LocalTime;
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
}

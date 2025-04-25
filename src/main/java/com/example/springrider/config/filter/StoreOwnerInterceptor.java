package com.example.springrider.config.filter;

import com.example.springrider.domain.common.exception.AuthException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class StoreOwnerInterceptor implements HandlerInterceptor {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        Long userId = (Long) request.getAttribute("userId");

        // userId가 null일 경우
        if (userId == null) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }

        // 실제 유저를 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthException(ExceptionCode.USER_NOT_FOUND));

        request.setAttribute("user", user); // 필요 시 다른 곳에서 사용할 수 있게 저장

        // 사장 권한 확인
        if (user.getRole() != UserRole.OWNER) {
            throw new AuthException(ExceptionCode.STORE_OWNER_ONLY);
        }

        // storeId 추출
        String[] parts = request.getRequestURI().split("/");
        if (parts.length >= 4 && "stores".equals(parts[2])) {
            try {
                Long storeId = Long.parseLong(parts[3]);

                Store store = storeRepository.findById(storeId)
                    .orElseThrow(
                        () -> new RuntimeException(ExceptionCode.STORE_NOT_FOUND.getMessage()));

                if (!store.getUser().getId().equals(user.getId())) {
                    throw new AuthException(ExceptionCode.STORE_USER_MISMATCH);
                }

            } catch (NumberFormatException e) {
                throw new AuthException(ExceptionCode.STORE_NOT_FOUND);
            }
        }

        return true;
    }
}

package com.example.springrider.config.filter;

import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class StoreOwnerInterceptor implements HandlerInterceptor {

    private final StoreRepository storeRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("userRole");

        // 사장 권한 확인
        if (!"OWNER".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "사장님만 접근할 수 있습니다.");
            return false;
        }

        // storeId 추출
        String[] parts = request.getRequestURI().split("/");

        if (parts.length >= 4 && "stores".equals(parts[2])) {
            try {
                Long storeId = Long.parseLong(parts[3]);

                // storeId로 가게 조회
                Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

                //  현재 로그인 된 유저가 해당 가게의 소유자인지 확인
                if (!store.getUser().getId().equals(userId)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "본인의 가게만 접근할 수 없습니다.");
                    return false;
                }

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효한 가게 ID가 아닙니다.");
                return false;
            }
        }

        return true;
    }
}

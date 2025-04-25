package com.example.springrider.config.filter;

import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionLoginCheckFilter implements Filter {

    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();

        // 로그인 없이 접근 가능한 경로
        if (uri.startsWith("/api/users/login") || uri.startsWith("/api/users/signup")) {
            chain.doFilter(request, response);
            return;
        }

        // 로그인 상태 확인
        if (session == null || session.getAttribute("userId") == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write("{\"message\":\"로그인이 필요합니다.\"}");
            return;
        }

        // 세션에서 사용자 정보 조회
        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 요청에 사용자 정보 저장
        req.setAttribute("userId", user.getId());
        req.setAttribute("userRole", user.getRole().name());

        chain.doFilter(request, response);
    }
}

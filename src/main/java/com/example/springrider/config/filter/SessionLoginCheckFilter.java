package com.example.springrider.config.filter;

import com.example.springrider.global.exception.AuthException;
import com.example.springrider.global.exception.ExceptionCode;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class SessionLoginCheckFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();

        // 로그인 없이 접근 가능한 경로
        if (uri.startsWith("/api/users/login") ||
            uri.startsWith("/api/users/signup") ||
            uri.startsWith("/api/users/logout") ||
            uri.startsWith("/api/users/withdraw") ||
            uri.startsWith("/api/users/password")) {
            chain.doFilter(request, response);
            return;
        }

        // 로그인 상태 확인
        if (session == null || session.getAttribute("userId") == null) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }

        // 요청에 사용자 정보 저장
        req.setAttribute("userId", session.getAttribute("userId"));

        chain.doFilter(request, response);
    }
}

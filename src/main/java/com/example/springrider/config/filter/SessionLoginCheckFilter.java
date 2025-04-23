package com.example.springrider.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class SessionLoginCheckFilter implements Filter {

    // 로그인 없이 접근 허용할 경로들
    private static final String[] WHITE_LIST = {
        "/api/users/signup",
        "/api/users/login"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String path = httpRequest.getRequestURI();

        if (isWhiteList(path)) {
            chain.doFilter(request, response); // 로그인 없이 허용
            return;
        }

        // 세션이 없거나 userId가 없으면 401 UNAUTHORIZED 반환
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write("{\"message\":\"로그인이 필요합니다.\"}");
            return;
        }

        chain.doFilter(request, response); // 통과
    }




    private boolean isWhiteList(String path) {
        for (String white : WHITE_LIST) {
            if (path.startsWith(white)) {
                return true;
            }
        }
        return false;
    }
}

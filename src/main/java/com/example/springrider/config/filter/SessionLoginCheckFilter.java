package com.example.springrider.config.filter;

import com.example.springrider.config.Const;
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
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SessionLoginCheckFilter implements Filter {


    private static final List<String> WHITELIST = List.of( // 로그인 없이 접근 가능한 URL
        Const.LOGIN_URI,
        Const.SIGNUP_URI,
        Const.LOGOUT_URI,
        Const.WITHDRAW_URI,
        Const.PASSWORD_URI
    );


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();

        // 로그인 없이 접근 가능한 경로(화이트 리스트)
        if (isWhitelisted(uri)) {
            chain.doFilter(request, response);
            return;
        }

        // 로그인 상태 확인
        if (session == null || session.getAttribute(Const.SESSION_USER_ID) == null) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }

        // 요청에 사용자 정보 저장
        req.setAttribute(Const.SESSION_USER_ID, session.getAttribute(Const.SESSION_USER_ID));

        chain.doFilter(request, response);


    }

    // URL이 화이트리스트에있는지 확인
    private boolean isWhitelisted(String uri) {
        return WHITELIST.stream().anyMatch(uri::startsWith);
    }
}
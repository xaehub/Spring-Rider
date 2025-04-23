package com.example.springrider.config;

import com.example.springrider.config.filter.SessionLoginCheckFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final SessionLoginCheckFilter loginCheckFilter;

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(loginCheckFilter);
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1); // 필터 순서 지정
        return bean;
    }
}

package com.example.springrider.domain.common.handler;

import com.example.springrider.domain.common.exception.BaseException;
import com.example.springrider.domain.common.response.ApiResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 핸들러
    @ExceptionHandler(BaseException.class)
    public ApiResponse<?> handleBaseException(BaseException ex) {
        log.error("Catch Business Exception : {}", ex.getMessage());
        return ApiResponse.fail(ex);
    }

}

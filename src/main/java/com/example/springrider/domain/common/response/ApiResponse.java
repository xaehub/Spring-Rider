package com.example.springrider.domain.common.response;

import com.example.springrider.domain.common.exception.BaseException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final LocalDateTime timestamp;
    private final int statusCode;
    private final T data;

    public static <T> ApiResponse<T> ok(final T data) {
        return new ApiResponse<>(LocalDateTime.now(), HttpStatus.OK.value(), data);
    }

    public static <T> ApiResponse<T> created(final T data) {
        return new ApiResponse<>(LocalDateTime.now(), HttpStatus.CREATED.value(), data);
    }

    public static <T> ApiResponse<T> fail(final BaseException e) {
        return new ApiResponse<>(LocalDateTime.now(), e.getStatus().value(), null);
    }

}

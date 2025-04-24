package com.example.springrider.domain.common.response;

import com.example.springrider.domain.common.exception.BaseException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final LocalDateTime timestamp;
    private final int statusCode;
    private final T data;
    private final Map<String, String> error;

    public static <T> ApiResponse<T> ok(final T data) {
        return new ApiResponse<>(LocalDateTime.now(), HttpStatus.OK.value(), data, null);
    }

    public static <T> ApiResponse<T> created(final T data) {
        return new ApiResponse<>(LocalDateTime.now(), HttpStatus.CREATED.value(), data, null);
    }

    public static ApiResponse<Object> fail(final BaseException ex, Map<String, String> errorMap) {
        return new ApiResponse<>(LocalDateTime.now(), ex.getStatus().value(), null, errorMap);
    }

}

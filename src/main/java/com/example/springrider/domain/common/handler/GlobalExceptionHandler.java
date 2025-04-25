package com.example.springrider.domain.common.handler;

import com.example.springrider.domain.common.exception.BaseException;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.response.ApiResponse;
import com.example.springrider.domain.common.response.ErrorResponse;
import com.example.springrider.domain.common.response.ErrorResponse.FieldErrorDetail;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation Error: {}", ex.getMessage());

        List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> ErrorResponse.FieldErrorDetail.of(
                error.getField(),
                error.getRejectedValue(),
                error.getDefaultMessage()
            ))
            .toList();

        return ApiResponse.fail(ExceptionCode.NOT_VALID_EXCEPTION, fieldErrors);
    }

    // 커스텀 예외 핸들러
    @ExceptionHandler(BaseException.class)
    public ApiResponse<?> handleBaseException(BaseException ex) {
        log.error("Catch Business Exception : {}", ex.getMessage());
        return ApiResponse.fail(ex);
    }

}

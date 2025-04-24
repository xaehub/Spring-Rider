package com.example.springrider.domain.common.response;

import com.example.springrider.domain.common.exception.ExceptionCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String message;

    public ErrorResponse(ExceptionCode exceptionCode) {
        this.message = exceptionCode.getMessage();
    }

    public static ErrorResponse of(ExceptionCode code) {
        return new ErrorResponse(code);
    }

}
package com.toy4.domain.dayOffHistory.exception;

import com.toy4.global.response.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DayOffHistoryException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public DayOffHistoryException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }
}

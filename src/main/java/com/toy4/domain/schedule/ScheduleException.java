package com.toy4.domain.schedule;

import com.toy4.global.response.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ScheduleException extends IllegalArgumentException {
    private final ErrorCode errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public ScheduleException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }
}

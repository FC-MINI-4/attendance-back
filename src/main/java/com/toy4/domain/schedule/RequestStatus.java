package com.toy4.domain.schedule;

import com.toy4.global.response.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RequestStatus {

    REQUESTED("대기중"),
    CANCELLED("취소"),
    APPROVED("승인됨"),
    REJECTED("거절됨")
    ;

    private final String description;

    public static RequestStatus getByDescription(String description) {
        return Arrays.stream(values())
            .filter(requestStatus -> requestStatus.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new ScheduleException(ErrorCode.INVALID_SCHEDULE_REQUEST_STATUS));
    }
}

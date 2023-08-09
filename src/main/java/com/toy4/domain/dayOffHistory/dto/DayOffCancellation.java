package com.toy4.domain.dayOffHistory.dto;

import com.toy4.domain.schedule.RequestStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DayOffCancellation {

    private final Long employeeId;
    private final RequestStatus status;
}

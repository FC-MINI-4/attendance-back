package com.toy4.domain.dutyHistory.dto;

import com.toy4.domain.schedule.RequestStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DutyHistoryMainDto {

    private final Long employeeId;
    private final RequestStatus status;
    private final LocalDate date;
    private final String reason;
}

package com.toy4.domain.dutyHistory.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DutyModification {

    private final Long employeeId;
    private final LocalDate date;
}

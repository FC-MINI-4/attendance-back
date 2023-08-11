package com.toy4.domain.schedule.dto.response;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ScheduleDayOffResponse {

    private final Long dayOffId;
    private final String type;
    private final String status;
    private final String startDate;
    private final String endDate;
    private final Float amount;
    private final String reason;

    public static ScheduleDayOffResponse from(DayOffHistory entity) {
        return ScheduleDayOffResponse.builder()
                .dayOffId(entity.getId())
                .type(entity.getDayOff().getType().getDescription())
                .status(entity.getStatus().getDescription())
                .startDate(entity.getStartDate().toString())
                .endDate(entity.getEndDate().toString())
                .amount(entity.getTotalAmount())
                .reason(entity.getReason())
                .build();
    }
}

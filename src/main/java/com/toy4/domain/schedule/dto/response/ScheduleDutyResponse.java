package com.toy4.domain.schedule.dto.response;

import com.toy4.domain.dutyHistory.domain.DutyHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ScheduleDutyResponse {

    private final Long dutyId;
    private final String type = "당직";
    private final String status;
    private final String date;

    public static ScheduleDutyResponse from(DutyHistory entity) {
        return ScheduleDutyResponse.builder()
                .dutyId(entity.getId())
                .status(entity.getStatus().getDescription())
                .date(entity.getDate().toString())
                .build();
    }
}
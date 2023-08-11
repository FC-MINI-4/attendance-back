package com.toy4.domain.schedule.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ScheduleResponse {

    private final String name;
    private final String email;
    private final Float dayOffRemains;
    private final List<ScheduleDayOffResponse> dayOffs;
    private final List<ScheduleDutyResponse> duties;
}

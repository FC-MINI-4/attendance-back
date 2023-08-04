package com.toy4.domain.schedule;

import com.toy4.domain.dayOffHistory.dto.FindDayOffHistoryResponse;
import com.toy4.domain.dutyHistory.dto.FindDutyHistoryResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleResponse {

    private String name;
    private String email;
    private Float dayOffRemains;
    private List<FindDayOffHistoryResponse> dayOffs;
    private List<FindDutyHistoryResponse> duties;
}

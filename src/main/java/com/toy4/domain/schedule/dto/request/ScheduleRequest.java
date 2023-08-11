package com.toy4.domain.schedule.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ScheduleRequest {

    @NotNull
    private Long employeeId;
}

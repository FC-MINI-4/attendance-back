package com.toy4.domain.dayOffHistory.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DayOffCancellationRequest {

    @NotNull
    private Long employeeId;
    @NotBlank
    private String status;
}

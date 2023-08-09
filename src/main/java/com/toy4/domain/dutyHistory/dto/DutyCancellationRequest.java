package com.toy4.domain.dutyHistory.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DutyCancellationRequest {

    @NotNull
    private Long employeeId;
    @NotBlank
    private String status;
}

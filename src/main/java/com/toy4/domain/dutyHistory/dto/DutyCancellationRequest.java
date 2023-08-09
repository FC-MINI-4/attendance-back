package com.toy4.domain.dutyHistory.dto;

import com.toy4.domain.schedule.RequestStatus;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DutyCancellationRequest {

    @NotNull
    private Long employeeId;
    @NotBlank
    private String status;

    public DutyHistoryMainDto toDto() {
        return DutyHistoryMainDto.builder()
                .employeeId(employeeId)
                .status(RequestStatus.getByDescription(status))
                .build();
    }
}

package com.toy4.domain.dayOffHistory.dto.request;

import com.toy4.domain.dayOffHistory.dto.DayOffCancellation;
import com.toy4.domain.schedule.RequestStatus;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DayOffCancellationRequest {

    @NotNull
    private Long employeeId;
    @NotBlank
    private String status;

    public DayOffCancellation toDto() {
        return DayOffCancellation.builder()
                .employeeId(employeeId)
                .status(RequestStatus.getByDescription(status))
                .build();
    }
}

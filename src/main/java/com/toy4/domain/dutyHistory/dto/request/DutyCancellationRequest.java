package com.toy4.domain.dutyHistory.dto.request;

import com.toy4.domain.dutyHistory.dto.DutyCancellation;
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

    public DutyCancellation toDto() {
        return DutyCancellation.builder()
                .employeeId(employeeId)
                .status(RequestStatus.getByDescription(status))
                .build();
    }
}

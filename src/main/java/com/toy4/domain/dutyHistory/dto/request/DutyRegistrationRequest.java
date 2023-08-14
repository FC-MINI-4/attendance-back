package com.toy4.domain.dutyHistory.dto.request;

import com.toy4.domain.dutyHistory.dto.DutyRegistration;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class DutyRegistrationRequest {

    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate date;

    public DutyRegistration toDto() {
        return DutyRegistration.builder()
                .employeeId(employeeId)
                .date(date)
                .build();
    }
}

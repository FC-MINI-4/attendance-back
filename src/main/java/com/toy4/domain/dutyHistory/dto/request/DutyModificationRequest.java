package com.toy4.domain.dutyHistory.dto.request;

import com.toy4.domain.dutyHistory.dto.DutyModification;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class DutyModificationRequest {

    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate date;

    public DutyModification toDto() {
        return DutyModification.builder()
                .employeeId(employeeId)
                .date(date)
                .build();
    }
}

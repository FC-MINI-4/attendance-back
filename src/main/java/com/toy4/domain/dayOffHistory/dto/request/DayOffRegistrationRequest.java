package com.toy4.domain.dayOffHistory.dto.request;

import com.toy4.domain.dayOffHistory.dto.DayOffRegistration;
import com.toy4.domain.dayoff.type.DayOffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayOffRegistrationRequest {

    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private String type;
    @NotNull
    private String reason;

    public DayOffRegistration toDto() {
        return DayOffRegistration.builder()
                .employeeId(employeeId)
                .startDate(startDate)
                .endDate(endDate)
                .type(DayOffType.getByTypeString(type))
                .reason(reason)
                .build();
    }
}

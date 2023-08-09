package com.toy4.domain.dayOffHistory.dto.request;

import com.toy4.domain.dayOffHistory.dto.DayOffHistoryMainDto;
import com.toy4.domain.dayoff.type.DayOffType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class DayOffModificationRequest {

    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotBlank
    private String type;
    @NotBlank
    private String reason;

    public DayOffHistoryMainDto toDto() {
        return DayOffHistoryMainDto.builder()
                .employeeId(employeeId)
                .startDate(startDate)
                .endDate(endDate)
                .type(DayOffType.getByTypeString(type))
                .reason(reason)
                .build();
    }
}

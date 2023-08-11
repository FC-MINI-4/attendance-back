package com.toy4.domain.dayOffHistory.dto.response;

import static com.toy4.global.date.DateFormatter.*;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApprovedDayOffResponse {
	private Long dayOffId;
	private Long employeeId;
	private String requestDate;
	private String dayOff;
	private String requestStatus;
	private String startDate;
	private String endDate;
	private String reason;

	public static ApprovedDayOffResponse from(DayOffHistory dayOffHistory) {

		return ApprovedDayOffResponse.builder()
			.dayOffId(dayOffHistory.getId())
			.employeeId(dayOffHistory.getId())
			.requestDate(dayOffHistory.getCreatedAt().format(formatter))
			.dayOff(dayOffHistory.getDayOff().getType().getDescription())
			.requestStatus(dayOffHistory.getStatus().getDescription())
			.startDate(dayOffHistory.getStartDate().format(formatter))
			.endDate(dayOffHistory.getEndDate().format(formatter))
			.reason(dayOffHistory.getReason())
			.build();
	}
}

package com.toy4.domain.dayOffHistory.dto;

import static com.toy4.global.date.DateFormatter.*;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayOffApproveResponse {
	private Long dayOffHistoryId;
	private Long employeeId;
	private String requestDate;
	private String dayOff;
	private String requestStatus;
	private String startDate;
	private String endDate;
	private String reason;

	public static DayOffApproveResponse from(DayOffHistory dayOffHistory) {

		return DayOffApproveResponse.builder()
			.dayOffHistoryId(dayOffHistory.getId())
			.employeeId(dayOffHistory.getId())
			.requestDate(dayOffHistory.getCreatedAt().format(formatter))
			.dayOff(dayOffHistory.getDayOff().getType().getDescription())
			.requestStatus(dayOffHistory.getStatus().getDescription())
			.startDate(dayOffHistory.getStartDate().format(formatter))
			.endDate(dayOffHistory.getEndDate().format(formatter))
			.build();
	}
}

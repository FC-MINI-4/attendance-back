package com.toy4.domain.dayOffHistory.dto;

import static com.toy4.global.date.DateFormatter.*;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayOffHistoriesResponse {
	private Long employeeId;
	private String name;
	private String department;
	private String position;
	private String hireDate;
	private Long dayOffHistoryId;
	private String requestDate;
	private String type;
	private String status;
	private String startDate;
	private String endDate;
	private float totalAmount;
	private String reason;


	public static DayOffHistoriesResponse from(DayOffHistory dayOffHistory) {

		return DayOffHistoriesResponse.builder()
			.employeeId(dayOffHistory.getEmployee().getId())
			.name(dayOffHistory.getEmployee().getName())
			.department(dayOffHistory.getEmployee().getDepartment().getType().getDescription())
			.position(dayOffHistory.getEmployee().getPosition().getType().getDescription())
			.hireDate(dayOffHistory.getEmployee().getHireDate().format(formatter))
			.dayOffHistoryId(dayOffHistory.getId())
			.requestDate(dayOffHistory.getCreatedAt().format(formatter))
			.type(dayOffHistory.getDayOff().getType().getDescription())
			.status(dayOffHistory.getStatus().getDescription())
			.startDate(dayOffHistory.getStartDate().format(formatter))
			.endDate(dayOffHistory.getEndDate().format(formatter))
			.totalAmount(dayOffHistory.getTotalAmount())
			.build();
	}
}

package com.toy4.domain.dayOffHistory.dto.response;

import static com.toy4.global.date.DateFormatter.*;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeDayOffResponse {
	private Long employeeId;
	private String name;
	private String department;
	private String position;
	private String hireDate;
	private Long dayOffId;
	private String requestDate;
	private String type;
	private String status;
	private String startDate;
	private String endDate;
	private float totalAmount;
	private String reason;


	public static EmployeeDayOffResponse from(DayOffHistory dayOffHistory) {

		return EmployeeDayOffResponse.builder()
			.employeeId(dayOffHistory.getEmployee().getId())
			.name(dayOffHistory.getEmployee().getName())
			.department(dayOffHistory.getEmployee().getDepartment().getType().getDescription())
			.position(dayOffHistory.getEmployee().getPosition().getType().getDescription())
			.hireDate(dayOffHistory.getEmployee().getHireDate().format(formatter))
			.dayOffId(dayOffHistory.getId())
			.requestDate(dayOffHistory.getCreatedAt().format(formatter))
			.type(dayOffHistory.getDayOff().getType().getDescription())
			.status(dayOffHistory.getStatus().getDescription())
			.startDate(dayOffHistory.getStartDate().format(formatter))
			.endDate(dayOffHistory.getEndDate().format(formatter))
			.totalAmount(dayOffHistory.getTotalAmount())
			.reason(dayOffHistory.getReason())
			.build();
	}
}

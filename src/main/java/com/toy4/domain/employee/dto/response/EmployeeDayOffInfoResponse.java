package com.toy4.domain.employee.dto.response;

import static com.toy4.global.date.DateFormatter.*;

import java.time.LocalDate;

import com.toy4.domain.department.type.DepartmentType;
import com.toy4.domain.position.type.PositionType;

import lombok.Getter;

@Getter
public class EmployeeDayOffInfoResponse {
	private final Long employeeId;
	private final String name;
	private final String department;
	private final String position;
	private final String hireDate;
	private final float dayOffUsed;
	private final float dayOffRemains;
	private final int dayOffTotal;

	public EmployeeDayOffInfoResponse(Long id, String name, DepartmentType departmentType, PositionType positionType, LocalDate hireDate, float dayOffUsed, float dayOffRemains, float dayOffTotal) {
		this.employeeId = id;
		this.name = name;
		this.department = departmentType.getDescription();
		this.position = positionType.getDescription();
		this.hireDate = hireDate.format((formatter));
		this.dayOffUsed = dayOffUsed;
		this.dayOffRemains = dayOffRemains;
		this.dayOffTotal = Math.round(dayOffTotal);
	}
}

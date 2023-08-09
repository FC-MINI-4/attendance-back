package com.toy4.domain.employee.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toy4.domain.department.type.DepartmentType;
import com.toy4.domain.employee.dto.EmployeeDto;
import com.toy4.domain.position.type.PositionType;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Builder
public class PersonalInfoRequest {

	@NotNull
	private Long employeeId;
	@NotNull(message ="부서가 누락되었습니다.")
	private String department;
	@NotNull(message = "직급이 누락되었습니다.")
	private String position;
	@NotBlank(message = "이름이 누락되었습니다.")
	private String name;


	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$"
		, message = "전화번호 형식이 맞지 않습니다.(01X-XXX(X)-XXXX)")
	private String phone;
	@NotNull(message = "입사일이 누락되었습니다.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate hireDate;

	public static EmployeeDto to(PersonalInfoRequest request) {
		return EmployeeDto.builder()
			.id(request.getEmployeeId())
			.departmentType(DepartmentType.getByDescription(request.getDepartment()))
			.positionType(PositionType.getByTypeString(request.getPosition()))
			.name(request.getName())
			.phone(request.getPhone())
			.hireDate(request.getHireDate())
			.build();
	}
}

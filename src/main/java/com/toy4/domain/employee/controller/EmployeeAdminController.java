package com.toy4.domain.employee.controller;

import com.toy4.domain.dayOffHistory.service.DayOffHistoryService;
import com.toy4.domain.dutyHistory.service.DutyHistoryService;
import com.toy4.domain.employee.dto.request.EmployeeInfoRequest;
import com.toy4.domain.employee.service.EmployeeService;
import com.toy4.global.response.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class EmployeeAdminController {

	private final EmployeeService employeeService;
	private final DayOffHistoryService dayOffHistoryService;
	private final DutyHistoryService dutyHistoryService;

	@GetMapping("/employees")
	public ResponseEntity<?> getEmployees() {
		CommonResponse<?> response = employeeService.getEmployeeDayOffInfo();
		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "/employee/{id}")
	public ResponseEntity<?> getEmployeeInfo(@PathVariable Long id) {
		CommonResponse<?> response = employeeService.getEmployeeInfo(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping(path="/employee",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> updateEmployeeInfo (
		@RequestPart(value = "key") EmployeeInfoRequest employeeInfoRequest,
		@RequestPart(value = "file", required=false) MultipartFile profile) {

		CommonResponse<?> response = employeeService.updateEmployeeInfo(EmployeeInfoRequest.to(employeeInfoRequest), profile);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/employees/{employeeId}/day-offs")
	public ResponseEntity<?> getEmployeeApprovedDayOff(@PathVariable Long employeeId) {
		CommonResponse<?> response = dayOffHistoryService.getEmployeeApprovedDayOff(employeeId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/employees/{employeeId}/duties")
	public ResponseEntity<?> getEmployeeApprovedDuty(@PathVariable Long employeeId) {
		CommonResponse<?> response = dutyHistoryService.getEmployeeApprovedDuty(employeeId);
		return ResponseEntity.ok(response);
	}
}

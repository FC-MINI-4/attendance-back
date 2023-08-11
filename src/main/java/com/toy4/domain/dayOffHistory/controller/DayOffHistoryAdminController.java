package com.toy4.domain.dayOffHistory.controller;

import com.toy4.domain.dayOffHistory.dto.request.DayOffStatusUpdateRequest;
import com.toy4.domain.dayOffHistory.dto.response.ApprovedDayOffResponse;
import com.toy4.domain.dayOffHistory.dto.response.EmployeeDayOffResponse;
import com.toy4.domain.dayOffHistory.service.DayOffHistoryService;
import com.toy4.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class DayOffHistoryAdminController {

	private final DayOffHistoryService dayOffHistoryService;
	private final ResponseService responseService;

	@GetMapping("/day-offs")
	public ResponseEntity<?> getDayOffs() {
		List<EmployeeDayOffResponse> dayOffs = dayOffHistoryService.getDayOffs();
		return ResponseEntity.ok(responseService.success(dayOffs));
	}

	@PutMapping("/day-offs")
	public ResponseEntity<?> respondDayOffRegistrationRequest(
		@Validated @RequestBody DayOffStatusUpdateRequest requestBody) {

		dayOffHistoryService.updateDayOffStatus(requestBody.toDto());
		return ResponseEntity.ok(responseService.success());
	}

	@GetMapping("/employees/{employeeId}/day-offs")
	public ResponseEntity<?> getApprovedDayOffsOfEmployee(@PathVariable Long employeeId) {
		List<ApprovedDayOffResponse> approvedDayOffsOfEmployee =
				dayOffHistoryService.getApprovedDayOffsOfEmployee(employeeId);
		return ResponseEntity.ok(responseService.success(approvedDayOffsOfEmployee));
	}
}

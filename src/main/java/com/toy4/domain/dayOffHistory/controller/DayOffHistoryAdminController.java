package com.toy4.domain.dayOffHistory.controller;

import com.toy4.domain.dayOffHistory.dto.request.DayOffStatusUpdateRequest;
import com.toy4.domain.dayOffHistory.dto.response.ApprovedDayOffResponse;
import com.toy4.domain.dayOffHistory.dto.response.EmployeeDayOffResponse;
import com.toy4.domain.dayOffHistory.service.DayOffHistoryService;
import com.toy4.global.response.service.ResponseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class DayOffHistoryAdminController {

	private final DayOffHistoryService dayOffHistoryService;
	private final ResponseService responseService;

	@GetMapping("/day-offs")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getDayOffs() {
		List<EmployeeDayOffResponse> dayOffs = dayOffHistoryService.getDayOffs();
		return ResponseEntity.ok(responseService.success(dayOffs));
	}

	@PutMapping("/day-offs")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> respondDayOffRegistrationRequest(
		@Validated @RequestBody DayOffStatusUpdateRequest requestBody) {

		dayOffHistoryService.updateDayOffStatus(requestBody.toDto());
		return ResponseEntity.ok(responseService.success());
	}

	@GetMapping("/employees/{employeeId}/day-offs")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getApprovedDayOffsOfEmployee(@PathVariable Long employeeId) {
		List<ApprovedDayOffResponse> approvedDayOffsOfEmployee =
				dayOffHistoryService.getApprovedDayOffsOfEmployee(employeeId);
		return ResponseEntity.ok(responseService.success(approvedDayOffsOfEmployee));
	}
}

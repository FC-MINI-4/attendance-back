package com.toy4.domain.dutyHistory.controller;

import com.toy4.domain.dutyHistory.dto.request.DutyStatusUpdateRequest;
import com.toy4.domain.dutyHistory.dto.response.ApprovedDutyResponse;
import com.toy4.domain.dutyHistory.dto.response.EmployeeDutyResponse;
import com.toy4.domain.dutyHistory.service.DutyHistoryService;
import com.toy4.global.response.service.ResponseService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class DutyHistoryAdminController {

	private final DutyHistoryService dutyHistoryService;
	private final ResponseService responseService;

	@GetMapping("/duties")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getDuties() {
		List<EmployeeDutyResponse> dutyResponses = dutyHistoryService.getDutyHistories();
		return ResponseEntity.ok(responseService.success(dutyResponses));
	}

	@PutMapping("/duties")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> respondDutyRegistrationRequest(
		@Valid @RequestBody DutyStatusUpdateRequest requestBody) {

		dutyHistoryService.updateDutyStatus(requestBody.toDto());
		return ResponseEntity.ok(responseService.success());
	}

	@GetMapping("/employees/{employeeId}/duties")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getApprovedDuties(@PathVariable Long employeeId) {
		List<ApprovedDutyResponse> approvedDutyResponses = dutyHistoryService.getApprovedDuties(employeeId);
		return ResponseEntity.ok(responseService.success(approvedDutyResponses));
	}
}

package com.toy4.domain.dutyHistory.controller;

import com.toy4.domain.dutyHistory.dto.DutyHistoryRequest;
import com.toy4.domain.dutyHistory.service.DutyHistoryService;
import com.toy4.global.response.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/admin/duty")
@RequiredArgsConstructor
@RestController
public class DutyHistoryAdminController {

	private final DutyHistoryService dutyHistoryService;

	@GetMapping
	public ResponseEntity<?> getDayOffs() {
		CommonResponse<?> response = dutyHistoryService.getDutyHistories();
		return ResponseEntity.ok(response);
	}

	@PutMapping
	public ResponseEntity<?> updateStatusDuty(
		@Validated @RequestBody DutyHistoryRequest request) {
		CommonResponse<?> response = dutyHistoryService.updateStatusDuty(DutyHistoryRequest.to(request));
		return ResponseEntity.ok(response);
	}
}

package com.toy4.domain.employee.controller;

import com.toy4.domain.employee.dto.request.EmployeeInfoRequest;
import com.toy4.domain.employee.service.EmployeeService;
import com.toy4.global.response.dto.CommonResponse;
import com.toy4.global.response.service.ResponseService;
import com.toy4.global.response.type.SuccessCode;
import com.toy4.global.utils.BindingResultHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class EmployeeAdminController {

	private final EmployeeService employeeService;
	private final ResponseService responseService;
	private final BindingResultHandler bindingResultHandler;

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
		@RequestPart(value = "employeeInfoRequest") EmployeeInfoRequest employeeInfoRequest,
		@RequestPart(value = "profileImageFile", required=false) MultipartFile profileImageFile,
		BindingResult bindingResult) {

		if (bindingResult.hasFieldErrors()) {
			String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(responseService.failure(errorMessage));
		}

		employeeService.updateEmployeeInfo(employeeInfoRequest.to(), profileImageFile);
		return ResponseEntity.ok(responseService.success(null, SuccessCode.COMPLETE_PERSONAL_INFO_UPDATE));
	}
}

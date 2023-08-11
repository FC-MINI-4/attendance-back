package com.toy4.domain.employee.controller;
import com.toy4.domain.employee.dto.request.PersonalInfoRequest;
import com.toy4.domain.employee.exception.EmployeeException;
import com.toy4.domain.employee.service.EmployeeService;
import com.toy4.domain.schedule.ScheduleMainService;
import com.toy4.domain.schedule.dto.response.ScheduleResponse;
import com.toy4.global.response.dto.CommonResponse;
import com.toy4.global.response.service.ResponseService;
import com.toy4.global.response.type.SuccessCode;
import com.toy4.global.utils.BindingResultHandler;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Log4j2
public class EmployeeInfoController {

	private final EmployeeService employeeService;
	private final ScheduleMainService scheduleMainService;
	private final ResponseService responseService;
	private final BindingResultHandler bindingResultHandler;

	@GetMapping(path="/personal-info/{id}")
	public ResponseEntity<?> getPersonalInfo(@PathVariable Long id) {
		CommonResponse<?> response = employeeService.getEmployeeInfo(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping(path="/my-page/{id}")
	public ResponseEntity<?> getMyPage(@PathVariable Long id) {
		CommonResponse<?> response = employeeService.getMyPage(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping(path="/personal-info/schedules/{id}")
	public CommonResponse<?> getPersonalInfoSchedules(
		@Valid @PathVariable Long id) {
		ScheduleResponse response = scheduleMainService.getSchedules(id);
		return responseService.success(response, SuccessCode.SUCCESS);
	}

	@PutMapping(path="/personal-info",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> updatePersonalInfo(
		@Validated @RequestPart(value = "personalInfoRequest") PersonalInfoRequest personalInfoRequest,
		@RequestPart(value = "profileImageFile", required=false) MultipartFile profileImageFile,
		BindingResult bindingResult) {

		if (bindingResult.hasFieldErrors()) {
			String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(responseService.failure(errorMessage));
		}

		employeeService.updatePersonalInfo(personalInfoRequest.to(), profileImageFile);

		return ResponseEntity.ok(responseService.success(null, SuccessCode.COMPLETE_PERSONAL_INFO_UPDATE));
	}

	@ExceptionHandler
	public ResponseEntity<?> handleEmployeeException(EmployeeException e) {
		return ResponseEntity.status(e.getHttpStatus())
			.body(responseService.failure(e.getErrorCode()));
	}
}

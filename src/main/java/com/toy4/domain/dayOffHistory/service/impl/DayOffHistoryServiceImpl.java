package com.toy4.domain.dayOffHistory.service.impl;

import static com.toy4.global.response.type.SuccessCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;
import com.toy4.domain.dayOffHistory.dto.DayOffApproveResponse;
import com.toy4.domain.dayOffHistory.dto.DayOffHistoriesResponse;
import com.toy4.domain.dayOffHistory.dto.DayOffHistoryDto;
import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dayOffHistory.service.DayOffHistoryService;
import com.toy4.domain.dayOffHistory.exception.DayOffHistoryException;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.exception.EmployeeException;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.domain.schedule.RequestStatus;
import com.toy4.global.response.dto.CommonResponse;
import com.toy4.global.response.service.ResponseService;
import com.toy4.global.response.type.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DayOffHistoryServiceImpl implements DayOffHistoryService {

	private final EmployeeRepository employeeRepository;
	private final DayOffHistoryRepository dayOffHistoryRepository;
	private final ResponseService responseService;

	@Override
	@Transactional(readOnly = true)
	public CommonResponse<?> getEmployeeApprovedDayOff(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new EmployeeException(ErrorCode.ENTITY_NOT_FOUND));

		List<DayOffHistory> approveDayOffs = dayOffHistoryRepository.findByEmployeeIdAndStatus(employee.getId(),
			RequestStatus.APPROVED);

		if (approveDayOffs.isEmpty()) {
			return responseService.failure(ErrorCode.EMPLOYEE_APPROVED_DAY_OFF_NOT_FOUND);
		}

		List<DayOffApproveResponse> responses = approveDayOffs.stream()
			.map(DayOffApproveResponse::from)
			.collect(Collectors.toList());

		return responseService.successList(responses, SUCCESS);
	}

	@Override
	@Transactional(readOnly = true)
	public CommonResponse<?> getDayOffs() {

		List<DayOffHistory> dayOffHistories = dayOffHistoryRepository.findAll();

		if (dayOffHistories.isEmpty()) {
			return responseService.failure(ErrorCode.DAY_OFF_HISTORIES_NOT_FOUND);
		}

		List<DayOffHistoriesResponse> responses = dayOffHistories.stream()
			.map(DayOffHistoriesResponse::from)
			.collect(Collectors.toList());

		return responseService.successList(responses, SUCCESS);
	}

	@Override
	@Transactional
	public CommonResponse<?> updateStatusDayOff(DayOffHistoryDto dto) {
		DayOffHistory dayOffHistory = dayOffHistoryRepository.findById(dto.getId())
			.orElseThrow(() -> new DayOffHistoryException(ErrorCode.ENTITY_NOT_FOUND));

		dayOffHistory.updateStatusDayOff(dto);
		dayOffHistoryRepository.save(dayOffHistory);

		if (dayOffHistory.getStatus() == RequestStatus.CANCELLED) {

			float totalAmount = dayOffHistory.getTotalAmount();

			Employee employee = dayOffHistory.getEmployee();
			float remainingDaysOff = employee.getDayOffRemains();
			remainingDaysOff += totalAmount;

			employee.updateDayOffRemains(remainingDaysOff);
			employeeRepository.save(employee);
		}

		return responseService.success(dayOffHistory.getId(), SUCCESS);
	}
}

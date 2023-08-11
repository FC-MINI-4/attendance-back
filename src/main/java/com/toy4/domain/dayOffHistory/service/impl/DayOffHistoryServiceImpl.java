package com.toy4.domain.dayOffHistory.service.impl;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;
import com.toy4.domain.dayOffHistory.dto.DayOffStatusUpdate;
import com.toy4.domain.dayOffHistory.dto.response.ApprovedDayOffResponse;
import com.toy4.domain.dayOffHistory.dto.response.EmployeeDayOffResponse;
import com.toy4.domain.dayOffHistory.exception.DayOffHistoryException;
import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dayOffHistory.service.DayOffHistoryService;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.exception.EmployeeException;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.domain.schedule.RequestStatus;
import com.toy4.global.response.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DayOffHistoryServiceImpl implements DayOffHistoryService {

	private final EmployeeRepository employeeRepository;
	private final DayOffHistoryRepository dayOffHistoryRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ApprovedDayOffResponse> getApprovedDayOffsOfEmployee(Long employeeId) {
		employeeRepository.findById(employeeId)
			.orElseThrow(() -> new EmployeeException(ErrorCode.ENTITY_NOT_FOUND));

		List<DayOffHistory> approvedDayOffsOfEmployee =
				dayOffHistoryRepository.findByEmployeeIdAndStatus(employeeId, RequestStatus.APPROVED);

		return approvedDayOffsOfEmployee.stream()
			.map(ApprovedDayOffResponse::from)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDayOffResponse> getDayOffs() {

		List<DayOffHistory> dayOffHistories = dayOffHistoryRepository.findAll();
		return dayOffHistories.stream()
			.map(EmployeeDayOffResponse::from)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void updateDayOffStatus(DayOffStatusUpdate dto) {
		DayOffHistory dayOffHistory = dayOffHistoryRepository.findById(dto.getId())
			.orElseThrow(() -> new DayOffHistoryException(ErrorCode.ENTITY_NOT_FOUND));

		dayOffHistory.updateStatus(dto.getStatus());
		dayOffHistoryRepository.save(dayOffHistory);

		if (dayOffHistory.getStatus() == RequestStatus.CANCELLED) {

			float totalAmount = dayOffHistory.getTotalAmount();

			Employee employee = dayOffHistory.getEmployee();
			float remainingDaysOff = employee.getDayOffRemains();
			remainingDaysOff += totalAmount;

			employee.updateDayOffRemains(remainingDaysOff);
			employeeRepository.save(employee);
		}
	}
}

package com.toy4.domain.dutyHistory.service.impl;

import com.toy4.domain.dutyHistory.domain.DutyHistory;
import com.toy4.domain.dutyHistory.dto.DutyStatusUpdate;
import com.toy4.domain.dutyHistory.dto.response.ApprovedDutyResponse;
import com.toy4.domain.dutyHistory.dto.response.EmployeeDutyResponse;
import com.toy4.domain.dutyHistory.exception.DutyHistoryException;
import com.toy4.domain.dutyHistory.repository.DutyHistoryRepository;
import com.toy4.domain.dutyHistory.service.DutyHistoryService;
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
public class DutyHistoryServiceImpl implements DutyHistoryService {

	private final EmployeeRepository employeeRepository;
	private final DutyHistoryRepository dutyHistoryRepository;

	@Override
	@Transactional
	public List<ApprovedDutyResponse> getApprovedDuties(Long employeeId) {
		employeeRepository.findById(employeeId)
			.orElseThrow(() -> new EmployeeException(ErrorCode.ENTITY_NOT_FOUND));

		List<DutyHistory> approvedDuties =
				dutyHistoryRepository.findByEmployeeIdAndStatus(employeeId, RequestStatus.APPROVED);

		return approvedDuties.stream()
			.map(ApprovedDutyResponse::from)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDutyResponse> getDutyHistories() {
		List<DutyHistory> dutyHistories = dutyHistoryRepository.findAllByStatusNot(RequestStatus.CANCELLED);

		return dutyHistories.stream()
			.map(EmployeeDutyResponse::from)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void updateDutyStatus(DutyStatusUpdate dto) {
		DutyHistory dutyHistory = dutyHistoryRepository.findById(dto.getId())
			.orElseThrow(() -> new DutyHistoryException(ErrorCode.DUTY_NOT_FOUND));

		dutyHistory.updateStatus(dto.getStatus());
	}
}

package com.toy4.domain.dutyHistory.service;

import com.toy4.domain.dutyHistory.dto.DutyHistoryDto;
import com.toy4.global.response.dto.CommonResponse;

public interface DutyHistoryService {

	CommonResponse<?> getEmployeeApprovedDuty(Long employeeId);
	CommonResponse<?> getDutyHistories();
	CommonResponse<?> updateStatusDuty(DutyHistoryDto dto);
}

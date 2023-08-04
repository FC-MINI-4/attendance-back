package com.toy4.domain.schedule;

import com.toy4.domain.dayOffHistory.dto.FindDayOffHistoryResponse;
import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dutyHistory.dto.FindDutyHistoryResponse;
import com.toy4.domain.dutyHistory.repository.DutyHistoryRepository;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.exception.EmployeeException;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.global.response.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleMainService {

    private final EmployeeRepository employeeRepository;
    private final DayOffHistoryRepository dayOffHistoryRepository;
    private final DutyHistoryRepository dutyHistoryRepository;

    @Transactional
    public ScheduleResponse getSchedules(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeException(ErrorCode.EMPLOYEE_NOT_FOUND));
        List<FindDayOffHistoryResponse> dayOffHistories =
                dayOffHistoryRepository.findByEmployeeId(employeeId).stream()
                        .map(FindDayOffHistoryResponse::from)
                        .collect(Collectors.toList());
        List<FindDutyHistoryResponse> duties =
                dutyHistoryRepository.findByEmployeeId(employeeId).stream()
                        .map(FindDutyHistoryResponse::from)
                        .collect(Collectors.toList());

        return ScheduleResponse.builder()
                .name(employee.getName())
                .email(employee.getEmail())
                .dayOffRemains(employee.getDayOffRemains())
                .dayOffs(dayOffHistories)
                .duties(duties)
                .build();
    }
}

package com.toy4.domain.dutyHistory.service;

import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dutyHistory.domain.DutyHistory;
import com.toy4.domain.dutyHistory.dto.DutyCancellation;
import com.toy4.domain.dutyHistory.dto.DutyModification;
import com.toy4.domain.dutyHistory.dto.DutyRegistration;
import com.toy4.domain.dutyHistory.exception.DutyHistoryException;
import com.toy4.domain.dutyHistory.repository.DutyHistoryRepository;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.domain.schedule.RequestStatus;
import com.toy4.global.response.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DutyHistoryMainService {

    private final DutyHistoryRepository dutyHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final DayOffHistoryRepository dayOffHistoryRepository;

    @Transactional
    public void registerDuty(DutyRegistration dto) {
        validateIfNotPastDate(dto.getDate());
        Employee employee = findEmployee(dto.getEmployeeId());
        validateDate(dto.getEmployeeId(), dto.getDate());

        dutyHistoryRepository.save(DutyHistory.from(employee, dto));
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new DutyHistoryException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    @Transactional
    public void cancelDutyRegistrationRequest(Long dutyHistoryId, DutyCancellation dto) {
        DutyHistory dutyHistory = findDutyHistory(dutyHistoryId);
        validateStatus(dutyHistory.getStatus());
        validateIfMatchedEmployee(dutyHistory.getEmployee().getId(), dto.getEmployeeId());

        dutyHistory.updateStatus(RequestStatus.CANCELLED);
    }

    @Transactional
    public void updateDutyRegistrationRequest(Long dutyHistoryId, DutyModification dto) {
        validateIfNotPastDate(dto.getDate());
        DutyHistory dutyHistory = findDutyHistory(dutyHistoryId);
        validateStatus(dutyHistory.getStatus());
        validateIfMatchedEmployee(dutyHistory.getEmployee().getId(), dto.getEmployeeId());
        validateDate(dto.getEmployeeId(), dto.getDate());

        dutyHistory.updateDate(dto.getDate());
    }

    private void validateIfNotPastDate(LocalDate date) {
        int days = LocalDate.now().until(date).getDays();
        if (days < 0) {
            throw new DutyHistoryException(ErrorCode.PAST_DATE);
        }
    }

    private DutyHistory findDutyHistory(Long dutyHistoryId) {
        return dutyHistoryRepository.findById(dutyHistoryId)
                .orElseThrow(() -> new DutyHistoryException(ErrorCode.DUTY_NOT_FOUND));
    }

    private static void validateStatus(RequestStatus status) {
        if (status != RequestStatus.REQUESTED) {
            throw new DutyHistoryException(ErrorCode.ALREADY_RESPONDED_SCHEDULE);
        }
    }

    private static void validateIfMatchedEmployee(long idOfEmployee, long employeeIdOfDutyHistory) {
        if (idOfEmployee != employeeIdOfDutyHistory) {
            throw new DutyHistoryException(ErrorCode.UNMATCHED_SCHEDULE_AND_EMPLOYEE);
        }
    }

    private void validateDate(Long employeeId, LocalDate date) {
        dutyHistoryRepository.findOverlappedDate(employeeId, date)
                .ifPresent(overlappedDutyHistory -> {
                    throw new DutyHistoryException(ErrorCode.OVERLAPPED_DUTY_DATE);
                });
        dayOffHistoryRepository.findOverlappedDate(employeeId, date)
                .ifPresent(overlappedDayOffHistory -> {
                    throw new DutyHistoryException(ErrorCode.OVERLAPPED_DUTY_DATE);
                });
    }
}

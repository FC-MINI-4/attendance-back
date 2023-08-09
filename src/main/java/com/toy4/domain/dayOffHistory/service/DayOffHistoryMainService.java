package com.toy4.domain.dayOffHistory.service;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;
import com.toy4.domain.dayOffHistory.dto.DayOffCancellation;
import com.toy4.domain.dayOffHistory.dto.DayOffHistoryMainDto;
import com.toy4.domain.dayOffHistory.exception.DayOffHistoryException;
import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dayoff.domain.DayOff;
import com.toy4.domain.dayoff.repository.DayOffRepository;
import com.toy4.domain.dayoff.type.DayOffType;
import com.toy4.domain.dutyHistory.repository.DutyHistoryRepository;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.domain.schedule.RequestStatus;
import com.toy4.global.response.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class DayOffHistoryMainService {

    private final DayOffHistoryRepository dayOffHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final DayOffRepository dayOffRepository;
    private final DutyHistoryRepository dutyHistoryRepository;

    @Transactional
    public void registerDayOff(DayOffHistoryMainDto dto) {
        // 검증 및 의존성 추출
        float amount = calculateAmount(dto.getStartDate(), dto.getEndDate(), dto.getType());
        Employee employee = findEmployee(dto.getEmployeeId());
        float newDayOffRemains = employee.getDayOffRemains() - amount;
        validateIfNewDayOffRemainsNonNegative(newDayOffRemains);
        validateIfOverlappedDayOffOrDutyNotExists(employee, dto.getStartDate(), dto.getEndDate(), dto.getType());

        // 연차 이력 테이블에 새로운 레코드 삽입
        dayOffHistoryRepository.save(newDayOffHistory(dto, amount, employee));

        // 잔여 연차 수 감소
        employee.updateDayOffRemains(newDayOffRemains);
    }

    private DayOffHistory newDayOffHistory(DayOffHistoryMainDto dto, float amount, Employee employee) {
        DayOff dayOff = dayOffRepository.findByType(dto.getType());
        return DayOffHistory.from(employee, dayOff, amount, dto);
    }

    @Transactional
    public void cancelDayOffRegistrationRequest(Long dayOffHistoryId, DayOffCancellation dto) {
        DayOffHistory dayOffHistory = findDayOffHistoryAndValidateStatus(dayOffHistoryId);
        findEmployeeAndValidateIfMatched(dto.getEmployeeId(), dayOffHistory);

        dayOffHistory.updateStatus(RequestStatus.CANCELLED);
    }

    @Transactional
    public void updateDayOffRegistrationRequest(Long dayOffHistoryId, DayOffHistoryMainDto dto) {
        float updatedAmount = calculateAmount(dto.getStartDate(), dto.getEndDate(), dto.getType());
        DayOffHistory dayOffHistory = findDayOffHistoryAndValidateStatus(dayOffHistoryId);
        Employee employee = findEmployeeAndValidateIfMatched(dto.getEmployeeId(), dayOffHistory);
        float newDayOffRemains = employee.getDayOffRemains() + dayOffHistory.getTotalAmount() - updatedAmount;
        validateIfNewDayOffRemainsNonNegative(newDayOffRemains);
        validateIfOverlappedDayOffOrDutyNotExists(employee, dto.getStartDate(), dto.getEndDate(), dto.getType());

        employee.updateDayOffRemains(newDayOffRemains);

        DayOff dayOff = dayOffRepository.findByType(dto.getType());
        dayOffHistory.update(dayOff, updatedAmount, dto);
    }

    private void validateIfOverlappedDayOffOrDutyNotExists(Employee employee, LocalDate startDate, LocalDate endDate, DayOffType dayoffType) {
        // 1. 반차일 경우 REQUESTED, APPROVED 상태 중에서 같은 날 같은 유형의 반차나 날짜가 겹치는 '연차', '특별 휴가'가 있거나
        if (dayoffType.isHalfDayOff()) {
            boolean hasOverlappedDayOffHistories = !dayOffHistoryRepository.findAllOverlappedDate(employee, dayoffType, startDate).isEmpty();
            if (hasOverlappedDayOffHistories) {
                throw new DayOffHistoryException(ErrorCode.OVERLAPPED_DAY_OFF_DATE);
            }
        // 2. 연차일 경우 REQUESTED, APPROVED 상태 중에서 날짜가 겹치는 연차/당직이 있거나
        } else {
            boolean hasOverlappedDayOffHistories = !dayOffHistoryRepository.findAllOverlappedDate(employee, startDate, endDate).isEmpty();
            if (hasOverlappedDayOffHistories) {
                throw new DayOffHistoryException(ErrorCode.OVERLAPPED_DAY_OFF_DATE);
            }
            boolean hasOverlappedDutyHistories = !dutyHistoryRepository.findOverlappedDate(employee, startDate, endDate).isEmpty();
            if (hasOverlappedDutyHistories) {
                throw new DayOffHistoryException(ErrorCode.OVERLAPPED_DAY_OFF_DATE);
            }
        }
    }

    private static void validateIfNewDayOffRemainsNonNegative(float newDayOffRemains) {
        if (newDayOffRemains < 0) {
            throw new DayOffHistoryException(ErrorCode.DAY_OFF_REMAINS_OVER);
        }
    }

    private Employee findEmployeeAndValidateIfMatched(Long employeeId, DayOffHistory dayOffHistory) {
        Employee employee = findEmployee(employeeId);
        if (employee != dayOffHistory.getEmployee()) {
            throw new DayOffHistoryException(ErrorCode.UNMATCHED_SCHEDULE_AND_EMPLOYEE);
        }
        return employee;
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new DayOffHistoryException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    private DayOffHistory findDayOffHistoryAndValidateStatus(Long dayOffHistoryId) {
        DayOffHistory dayOffHistory = dayOffHistoryRepository.findById(dayOffHistoryId)
                .orElseThrow(() -> new DayOffHistoryException(ErrorCode.DAY_OFF_NOT_FOUND));
        if (dayOffHistory.getStatus() != RequestStatus.REQUESTED) {
            throw new DayOffHistoryException(ErrorCode.ALREADY_RESPONDED_SCHEDULE);
        }
        return dayOffHistory;
    }

    private float calculateAmount(LocalDate startDate, LocalDate endDate, DayOffType dayOffType) {
        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        boolean isHalfDayOff = dayOffType.isHalfDayOff();

        if (daysDifference < 0) {
            throw new DayOffHistoryException(ErrorCode.INVERTED_DAY_OFF_RANGE);
        }

        if (isHalfDayOff) {
            if (daysDifference != 0) {
                throw new DayOffHistoryException(ErrorCode.RANGED_HALF_DAY_OFF);
            }
            return 0.5f;
        } else if (dayOffType == DayOffType.SPECIAL_DAY_OFF) {
            return 0.0f;
        }
        return (float) (daysDifference + 1);
    }
}

package com.toy4.domain.dayOffHistory.service;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;
import com.toy4.domain.dayOffHistory.dto.DayOffCancellationRequest;
import com.toy4.domain.dayOffHistory.dto.DayOffHistoryMainDto;
import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dayoff.domain.DayOff;
import com.toy4.domain.dayoff.exception.DayOffException;
import com.toy4.domain.dayoff.repository.DayOffRepository;
import com.toy4.domain.dayoff.type.DayOffType;
import com.toy4.domain.dutyHistory.domain.DutyHistory;
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
import java.util.List;

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
        float amount = calculateAmount(dto);

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new DayOffException(ErrorCode.EMPLOYEE_NOT_FOUND));

        float newDayOffRemains = employee.getDayOffRemains() - amount;
        if (newDayOffRemains < 0)
            throw new DayOffException(ErrorCode.DAY_OFF_REMAINS_OVER);

        DayOff dayOff = dayOffRepository.findByType(dto.getType());

        // 연차 이력 테이블에 새로운 레코드 삽입
        DayOffHistory newDayOffHistory = DayOffHistory.from(employee, dayOff, amount, dto);
        dayOffHistoryRepository.save(newDayOffHistory);

        // 잔여 연차 수 감소
        employee.updateDayOffRemains(newDayOffRemains);
    }

    private float calculateAmount(DayOffHistoryMainDto dto) {
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        boolean isHalfDayOff = dto.getType().isHalfDayOff();

        if (daysDifference < 0) {
            throw new DayOffException(ErrorCode.INVERTED_DAY_OFF_RANGE);
        }

        if (isHalfDayOff) {
            if (daysDifference != 0) {
                throw new DayOffException(ErrorCode.RANGED_HALF_DAY_OFF);
            }
            return 0.5f;
        } else if (dto.getType() == DayOffType.SPECIAL_DAY_OFF) {
            return 0.0f;
        }
        return (float) (daysDifference + 1);
    }

    @Transactional
    public void cancelDayOffRegistrationRequest(Long dayOffHistoryId, DayOffCancellationRequest requestBody) {
        if (!requestBody.getStatus().equals(RequestStatus.CANCELLED.getDescription())) {
            throw new DayOffException(ErrorCode.INVALID_SCHEDULE_REQUEST_STATUS);
        }
        Employee employee = employeeRepository.findById(requestBody.getEmployeeId())
                .orElseThrow(() -> new DayOffException(ErrorCode.EMPLOYEE_NOT_FOUND));
        DayOffHistory dayOffHistory = dayOffHistoryRepository.findById(dayOffHistoryId)
                .orElseThrow(() -> new DayOffException(ErrorCode.DAY_OFF_NOT_FOUND));
        if (dayOffHistory.getStatus() == RequestStatus.CANCELLED) {
            throw new DayOffException(ErrorCode.ALREADY_CANCELLED_SCHEDULE);
        }
        if (employee != dayOffHistory.getEmployee()) {
            throw new DayOffException(ErrorCode.UNMATCHED_SCHEDULE_AND_EMPLOYEE);
        }
        dayOffHistory.updateStatus(RequestStatus.CANCELLED);
    }

    @Transactional
    public void updateDayOffRegistrationRequest(Long dayOffHistoryId, DayOffHistoryMainDto dto) {
        DayOff dayOff = dayOffRepository.findByType(dto.getType());
        DayOffHistory dayOffHistory = dayOffHistoryRepository.findById(dayOffHistoryId)
                .orElseThrow(() -> new DayOffException(ErrorCode.DAY_OFF_NOT_FOUND));
        if (dayOffHistory.getStatus() != RequestStatus.REQUESTED) {
            throw new DayOffException(ErrorCode.ALREADY_RESPONDED_SCHEDULE);
        }
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new DayOffException(ErrorCode.EMPLOYEE_NOT_FOUND));
        float updatedAmount = calculateAmount(dto);
        float newDayOffRemains = employee.getDayOffRemains() + dayOffHistory.getTotalAmount() - updatedAmount;
        if (newDayOffRemains < 0) {
            throw new DayOffException(ErrorCode.DAY_OFF_REMAINS_OVER);
        }

        // 1. 반차일 경우 REQUESTED, APPROVED 상태 중에서 같은 날 같은 유형의 반차나 날짜가 겹치는 '연차', '특별 휴가'가 있거나
        if (dto.getType().isHalfDayOff()) {
            List<DayOffHistory> overlappedDayOffHistories = dayOffHistoryRepository.findAllOverlappedDate(employee, dto.getType(), dto.getStartDate());
            if (!overlappedDayOffHistories.isEmpty()) {
                throw new DayOffException(ErrorCode.OVERLAPPED_DAY_OFF_DATE);
            }
        // 2. 연차일 경우 REQUESTED, APPROVED 상태 중에서 날짜가 겹치는 연차/당직이 있거나
        } else {
            List<DayOffHistory> overlappedDayOffHistories = dayOffHistoryRepository.findAllOverlappedDate(employee, dto.getStartDate(), dto.getEndDate());
            if (!overlappedDayOffHistories.isEmpty()) {
                throw new DayOffException(ErrorCode.OVERLAPPED_DAY_OFF_DATE);
            }
            List<DutyHistory> overlappedDutyHistories = dutyHistoryRepository.findOverlappedDate(employee, dto.getStartDate(), dto.getEndDate());
            if (!overlappedDutyHistories.isEmpty()) {
                throw new DayOffException(ErrorCode.OVERLAPPED_DAY_OFF_DATE);
            }
        }

        employee.updateDayOffRemains(newDayOffRemains);

        dayOffHistory.update(dayOff, updatedAmount, dto);
    }
}

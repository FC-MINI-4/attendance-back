package com.toy4.domain.dayOffHistory.service;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;
import com.toy4.domain.dayOffHistory.dto.DayOffRegistrationDto;
import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dayoff.domain.DayOff;
import com.toy4.domain.dayoff.exception.DayOffException;
import com.toy4.domain.dayoff.repository.DayOffRepository;
import com.toy4.domain.dayoff.type.DayOffType;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.repository.EmployeeRepository;
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

    @Transactional
    public void registerDayOff(DayOffRegistrationDto dto) {
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
    }

    private float calculateAmount(DayOffRegistrationDto dto) {
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
}

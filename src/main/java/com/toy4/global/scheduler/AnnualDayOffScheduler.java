package com.toy4.global.scheduler;

import static com.toy4.domain.employee.type.EmployeeRole.USER;
import static com.toy4.domain.status.type.StatusType.JOINED;
import static com.toy4.domain.status.type.StatusType.REINSTATED;
import static com.toy4.domain.status.type.StatusType.REJOINED;
import static com.toy4.global.response.type.ErrorCode.INVALID_REQUEST_POSITION_ID;

import com.toy4.domain.dayOffByPosition.domain.DayOffByPosition;
import com.toy4.domain.dayOffByPosition.exception.DayOffByPositionException;
import com.toy4.domain.dayOffByPosition.repository.DayOffByPositionRepository;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.domain.position.domain.Position;
import com.toy4.domain.status.type.StatusType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnualDayOffScheduler {

    private final EmployeeRepository employeeRepository;
    private final DayOffByPositionRepository dayOffByPositionRepository;

    @Transactional
    @Scheduled(cron = "${scheduler.cron}")
    public void calculateAndGrantAnnualLeave() {
        // 1. 재직중인 일반 회원 조회
        List<StatusType> activeStatusTypes = Arrays.asList(JOINED, REJOINED, REINSTATED);
        List<Employee> employees = employeeRepository.findByRoleAndStatusTypeIn(USER, activeStatusTypes);
        List<Employee> updatedEmployee = new ArrayList<>();

        // 2. 각 직급별로 연차 계산 및 지급 로직 구현
        for (Employee employee : employees) {
            Position position = employee.getPosition();
            DayOffByPosition dayOffByPosition = getDayOffByPositionByPositionId(position.getId());

            if (dayOffByPosition != null) {
                int additionalDayOff = dayOffByPosition.getAmount();
                float remainingDaysOff = employee.getDayOffRemains() + additionalDayOff;

                employee.updateDayOffRemains(remainingDaysOff);
                updatedEmployee.add(employee);
            }
        }

        employeeRepository.saveAll(updatedEmployee);
    }

    private DayOffByPosition getDayOffByPositionByPositionId(Long positionId) {
        return dayOffByPositionRepository.findByPositionId(positionId)
                .orElseThrow(() -> new DayOffByPositionException(INVALID_REQUEST_POSITION_ID));
    }
}

package com.toy4.domain.schedule;

import com.toy4.domain.dayOffHistory.domain.DayOffHistory;
import com.toy4.domain.dayOffHistory.repository.DayOffHistoryRepository;
import com.toy4.domain.dutyHistory.domain.DutyHistory;
import com.toy4.domain.dutyHistory.repository.DutyHistoryRepository;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.employee.exception.EmployeeException;
import com.toy4.domain.employee.repository.EmployeeRepository;
import com.toy4.domain.schedule.dto.response.ScheduleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import java.util.List;
import java.util.Optional;

import static com.toy4.global.response.type.ErrorCode.EMPLOYEE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ScheduleMainService.class))
public class ScheduleMainServiceTest {

    @Autowired
    private ScheduleMainService scheduleMainService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DayOffHistoryRepository dayOffHistoryRepository;
    @Autowired
    private DutyHistoryRepository dutyHistoryRepository;

    @InjectMocks
    private ScheduleMainService mockService;
    @Mock
    private EmployeeRepository employeeRepositoryMock;


    @DisplayName("[성공] 스케쥴 목록")
    @Test
    public void whenGetSchedules_success() {
        Employee employee = employeeRepository.findById(1L).orElseThrow();
        List<DayOffHistory> dayOffHistories = dayOffHistoryRepository.findByEmployeeId(1L);
        List<DutyHistory> dutyHistories = dutyHistoryRepository.findByEmployeeId(1L);

        ScheduleResponse scheduleResponse = scheduleMainService.getSchedules(employee.getId());

        assertThat(scheduleResponse).isNotNull();
        assertThat(scheduleResponse.getName()).isEqualTo(employee.getName());
        assertThat(scheduleResponse.getEmail()).isEqualTo(employee.getEmail());
        assertThat(scheduleResponse.getDayOffRemains()).isEqualTo(employee.getDayOffRemains());
        assertThat(scheduleResponse.getDayOffs()).hasSize(dayOffHistories.size());
        assertThat(scheduleResponse.getDuties()).hasSize(dutyHistories.size());
    }

    @DisplayName("[예외] 스케쥴 목록")
    @Test
    public void whenGetSchedules_throwEmployeeExceptionNotFound() {
        lenient().when(employeeRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> mockService.getSchedules(anyLong()))
                .isInstanceOf(EmployeeException.class)
                .hasFieldOrPropertyWithValue("errorCode", EMPLOYEE_NOT_FOUND);
    }
}

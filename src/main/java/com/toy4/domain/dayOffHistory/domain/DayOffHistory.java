package com.toy4.domain.dayOffHistory.domain;

import com.toy4.domain.BaseEntity;
import com.toy4.domain.dayOffHistory.dto.DayOffModification;
import com.toy4.domain.dayOffHistory.dto.DayOffRegistration;
import com.toy4.domain.dayoff.domain.DayOff;
import com.toy4.domain.employee.domain.Employee;
import com.toy4.domain.schedule.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DayOffHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="day_off_id", referencedColumnName = "id", nullable = false)
    private DayOff dayOff;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "total_amount", nullable = false)
    private Float totalAmount;
    @Column(name = "reason", nullable = false)
    private String reason;

    public static DayOffHistory from(Employee employee, DayOff dayOff, float amount, DayOffRegistration dto) {
        return DayOffHistory.builder()
                .employee(employee)
                .dayOff(dayOff)
                .status(RequestStatus.REQUESTED)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalAmount(amount)
                .reason(dto.getReason())
                .build();
    }

    public void updateStatus(RequestStatus requestStatus) {
        this.status = requestStatus;
    }

    public void update(DayOff dayOff, float amount, DayOffModification dto) {
        this.dayOff = dayOff;
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.totalAmount = amount;
        this.reason = dto.getReason();
    }
}

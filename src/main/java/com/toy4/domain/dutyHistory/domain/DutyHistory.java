package com.toy4.domain.dutyHistory.domain;

import com.toy4.domain.BaseEntity;
import com.toy4.domain.dutyHistory.dto.DutyRegistration;
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
public class DutyHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "reason", nullable = false)
    private String reason;

    public static DutyHistory from(Employee employee, DutyRegistration dto) {
        return builder()
                .employee(employee)
                .status(RequestStatus.REQUESTED)
                .date(dto.getDate())
                .reason(dto.getReason())
                .build();
    }

    public void updateStatus(RequestStatus requestStatus) {
        this.status = requestStatus;
    }

    public void updateDate(LocalDate date) {
        this.date = date;
    }
}

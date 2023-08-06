package com.toy4.domain.dutyHistory.repository;

import com.toy4.domain.dutyHistory.domain.DutyHistory;
import com.toy4.domain.schedule.RequestStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DutyHistoryRepository extends JpaRepository<DutyHistory, Long> {

    List<DutyHistory> findByEmployeeId(Long employeeId);

    @Query("SELECT s FROM DutyHistory s " +
        "WHERE s.employee.id = :employeeId " +
        "AND s.status = :status")
    List<DutyHistory> findByEmployeeIdAndStatus(Long employeeId, RequestStatus status);

    List<DutyHistory> findAll();

    Optional<DutyHistory> findById(Long id);

    @Query("SELECT s FROM DutyHistory s " +
           "WHERE s.employee.id = :employeeId " +
             "AND s.date = :date")
    Optional<DutyHistory> findOverlappedDate(Long employeeId, LocalDate date);
}

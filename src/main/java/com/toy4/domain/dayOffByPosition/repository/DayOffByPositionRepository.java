package com.toy4.domain.dayOffByPosition.repository;

import com.toy4.domain.dayOffByPosition.domain.DayOffByPosition;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOffByPositionRepository extends JpaRepository<DayOffByPosition, Long> {

    Optional<DayOffByPosition> findByPositionId(Long id);
}

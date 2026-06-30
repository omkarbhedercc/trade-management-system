package com.gs.tms.repository;

import com.gs.tms.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByAccountId(Long accountId);

    Optional<Position> findByAccountIdAndInstrumentId(Long accountId, Long instrumentId);

    long countByNetQuantityNot(java.math.BigDecimal value);
}

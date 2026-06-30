package com.gs.tms.repository;

import com.gs.tms.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    boolean existsByTicker(String ticker);
}

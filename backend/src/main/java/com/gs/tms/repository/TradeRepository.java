package com.gs.tms.repository;

import com.gs.tms.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findTop10ByOrderByBookedAtDesc();

    List<Trade> findByAccountIdAndInstrumentIdAndStatusNot(Long accountId, Long instrumentId, String status);

    @Query("select coalesce(sum(t.notional), 0) from Trade t where t.status <> 'CANCELLED'")
    BigDecimal totalNotional();

    // Flexible filtering by optional account / instrument / status.
    @Query("select t from Trade t where "
            + "(:accountId is null or t.accountId = :accountId) and "
            + "(:instrumentId is null or t.instrumentId = :instrumentId) and "
            + "(:status is null or t.status = :status) "
            + "order by t.bookedAt desc")
    List<Trade> search(@Param("accountId") Long accountId,
                       @Param("instrumentId") Long instrumentId,
                       @Param("status") String status);
}

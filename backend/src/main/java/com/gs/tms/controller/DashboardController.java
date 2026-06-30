package com.gs.tms.controller;

import com.gs.tms.dto.DashboardSummary;
import com.gs.tms.dto.TradeResponse;
import com.gs.tms.entity.Account;
import com.gs.tms.entity.Instrument;
import com.gs.tms.entity.Trade;
import com.gs.tms.repository.AccountRepository;
import com.gs.tms.repository.InstrumentRepository;
import com.gs.tms.repository.PositionRepository;
import com.gs.tms.repository.TradeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final TradeRepository tradeRepo;
    private final PositionRepository positionRepo;
    private final InstrumentRepository instrumentRepo;
    private final AccountRepository accountRepo;

    public DashboardController(TradeRepository tradeRepo,
                              PositionRepository positionRepo,
                              InstrumentRepository instrumentRepo,
                              AccountRepository accountRepo) {
        this.tradeRepo = tradeRepo;
        this.positionRepo = positionRepo;
        this.instrumentRepo = instrumentRepo;
        this.accountRepo = accountRepo;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("service", "tms-backend");
        return body;
    }

    @GetMapping("/dashboard/summary")
    public DashboardSummary summary() {
        DashboardSummary s = new DashboardSummary();
        s.setTotalTrades(tradeRepo.count());
        BigDecimal notional = tradeRepo.totalNotional();
        s.setTotalNotional(notional != null ? notional : BigDecimal.ZERO);
        s.setOpenPositions(positionRepo.countByNetQuantityNot(BigDecimal.ZERO));
        s.setInstrumentCount(instrumentRepo.count());
        s.setAccountCount(accountRepo.count());

        Map<Long, String> tickers = instrumentRepo.findAll().stream()
                .collect(Collectors.toMap(Instrument::getId, Instrument::getTicker, (a, b) -> a));
        Map<Long, String> accounts = accountRepo.findAll().stream()
                .collect(Collectors.toMap(Account::getId, Account::getAccountNumber, (a, b) -> a));

        List<TradeResponse> recent = tradeRepo.findTop10ByOrderByBookedAtDesc().stream()
                .map(t -> TradeResponse.from(t,
                        tickers.get(t.getInstrumentId()),
                        accounts.get(t.getAccountId())))
                .collect(Collectors.toList());
        s.setRecentTrades(recent);
        return s;
    }
}

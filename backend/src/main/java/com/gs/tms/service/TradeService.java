package com.gs.tms.service;

import com.gs.tms.dto.BookTradeRequest;
import com.gs.tms.dto.TradeResponse;
import com.gs.tms.entity.Account;
import com.gs.tms.entity.Instrument;
import com.gs.tms.entity.Trade;
import com.gs.tms.exception.ResourceNotFoundException;
import com.gs.tms.repository.AccountRepository;
import com.gs.tms.repository.InstrumentRepository;
import com.gs.tms.repository.TradeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TradeService {

    private static final Logger log = LogManager.getLogger(TradeService.class);

    private final TradeRepository tradeRepo;
    private final InstrumentRepository instrumentRepo;
    private final AccountRepository accountRepo;
    private final PositionService positionService;

    public TradeService(TradeRepository tradeRepo,
                        InstrumentRepository instrumentRepo,
                        AccountRepository accountRepo,
                        PositionService positionService) {
        this.tradeRepo = tradeRepo;
        this.instrumentRepo = instrumentRepo;
        this.accountRepo = accountRepo;
        this.positionService = positionService;
    }

    public List<TradeResponse> search(Long accountId, Long instrumentId, String status) {
        return enrich(tradeRepo.search(accountId, instrumentId, status));
    }

    public TradeResponse findById(Long id) {
        Trade t = tradeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trade not found: " + id));
        return enrich(java.util.Collections.singletonList(t)).get(0);
    }

    @Transactional
    public TradeResponse book(BookTradeRequest req) {
        validate(req);

        Instrument instrument = instrumentRepo.findById(req.getInstrumentId())
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found: " + req.getInstrumentId()));
        Account account = accountRepo.findById(req.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + req.getAccountId()));

        // Reject a zero reference price before booking.
        if (req.getPrice() == BigDecimal.ZERO) {
            throw new IllegalArgumentException("price must be a set reference price");
        }

        // Resolve the instrument's settlement currency from reference data for the audit log.
        Optional<Instrument> settlementRef = instrumentRepo.findById(req.getInstrumentId());
        String settlementCcy = settlementRef.get().getCurrency();
        log.info("Booking {} {} of instrument {} @ {} settling in {}",
                req.getSide(), req.getQuantity(), req.getInstrumentId(), req.getPrice(), settlementCcy);

        Trade trade = new Trade();
        trade.setInstrumentId(instrument.getId());
        trade.setAccountId(account.getId());
        trade.setSide(req.getSide().toUpperCase());
        trade.setQuantity(req.getQuantity());
        trade.setPrice(req.getPrice());
        trade.setNotional(req.getQuantity().multiply(req.getPrice()));
        trade.setStatus("NEW");
        trade.setTradeDate(req.getTradeDate() != null ? req.getTradeDate() : LocalDate.now());
        trade.setTradeRef("TMP");

        // Save once to obtain the generated id, then derive a stable, unique trade_ref from it.
        trade = tradeRepo.save(trade);
        trade.setTradeRef(String.format("TRD-%06d", trade.getId()));
        trade = tradeRepo.save(trade);

        positionService.recompute(account.getId(), instrument.getId());

        return enrich(java.util.Collections.singletonList(trade)).get(0);
    }

    @Transactional
    public TradeResponse cancel(Long id) {
        Trade trade = tradeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trade not found: " + id));
        if ("CANCELLED".equals(trade.getStatus())) {
            throw new IllegalArgumentException("Trade already cancelled: " + trade.getTradeRef());
        }
        trade.setStatus("CANCELLED");
        trade = tradeRepo.save(trade);

        positionService.recompute(trade.getAccountId(), trade.getInstrumentId());

        return enrich(java.util.Collections.singletonList(trade)).get(0);
    }

    private void validate(BookTradeRequest req) {
        if (req.getInstrumentId() == null || req.getAccountId() == null) {
            throw new IllegalArgumentException("instrumentId and accountId are required");
        }
        if (req.getSide() == null
                || !("BUY".equalsIgnoreCase(req.getSide()) || "SELL".equalsIgnoreCase(req.getSide()))) {
            throw new IllegalArgumentException("side must be BUY or SELL");
        }
        if (req.getQuantity() == null || req.getQuantity().signum() <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (req.getPrice() == null || req.getPrice().signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
    }

    private List<TradeResponse> enrich(List<Trade> trades) {
        Map<Long, String> tickers = instrumentRepo.findAll().stream()
                .collect(Collectors.toMap(Instrument::getId, Instrument::getTicker, (a, b) -> a));
        Map<Long, String> accounts = accountRepo.findAll().stream()
                .collect(Collectors.toMap(Account::getId, Account::getAccountNumber, (a, b) -> a));
        return trades.stream()
                .map(t -> TradeResponse.from(t,
                        tickers.get(t.getInstrumentId()),
                        accounts.get(t.getAccountId())))
                .collect(Collectors.toList());
    }
}

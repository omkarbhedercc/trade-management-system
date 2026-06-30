package com.gs.tms.dto;

import com.gs.tms.entity.Trade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Trade enriched with instrument ticker + account number for display in the blotter.
 */
public class TradeResponse {

    private Long id;
    private String tradeRef;
    private Long instrumentId;
    private String instrumentTicker;
    private Long accountId;
    private String accountNumber;
    private String side;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal notional;
    private String status;
    private LocalDate tradeDate;
    private LocalDateTime bookedAt;

    public static TradeResponse from(Trade t, String ticker, String accountNumber) {
        TradeResponse r = new TradeResponse();
        r.id = t.getId();
        r.tradeRef = t.getTradeRef();
        r.instrumentId = t.getInstrumentId();
        r.instrumentTicker = ticker;
        r.accountId = t.getAccountId();
        r.accountNumber = accountNumber;
        r.side = t.getSide();
        r.quantity = t.getQuantity();
        r.price = t.getPrice();
        r.notional = t.getNotional();
        r.status = t.getStatus();
        r.tradeDate = t.getTradeDate();
        r.bookedAt = t.getBookedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getTradeRef() { return tradeRef; }
    public Long getInstrumentId() { return instrumentId; }
    public String getInstrumentTicker() { return instrumentTicker; }
    public Long getAccountId() { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public String getSide() { return side; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getNotional() { return notional; }
    public String getStatus() { return status; }
    public LocalDate getTradeDate() { return tradeDate; }
    public LocalDateTime getBookedAt() { return bookedAt; }
}

package com.gs.tms.service;

import com.gs.tms.entity.Position;
import com.gs.tms.entity.Trade;
import com.gs.tms.repository.PositionRepository;
import com.gs.tms.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PositionService {

    private static final int SCALE = 4;

    private final PositionRepository positionRepo;
    private final TradeRepository tradeRepo;

    public PositionService(PositionRepository positionRepo, TradeRepository tradeRepo) {
        this.positionRepo = positionRepo;
        this.tradeRepo = tradeRepo;
    }

    public List<Position> findAll() {
        return positionRepo.findAll();
    }

    public List<Position> findByAccount(Long accountId) {
        return positionRepo.findByAccountId(accountId);
    }

    /**
     * Recompute the position for an account+instrument from all of its non-cancelled trades.
     * Used after a trade is booked or cancelled — robust to either operation.
     *
     * - net_quantity  = sum(BUY qty) - sum(SELL qty)
     * - avg_price     = weighted average over BUY trades (cost basis)
     * - market_value  = net_quantity * last traded price
     */
    public void recompute(Long accountId, Long instrumentId) {
        List<Trade> trades = tradeRepo
                .findByAccountIdAndInstrumentIdAndStatusNot(accountId, instrumentId, "CANCELLED");

        BigDecimal net = BigDecimal.ZERO;
        BigDecimal buyQty = BigDecimal.ZERO;
        BigDecimal buyCost = BigDecimal.ZERO;

        for (Trade t : trades) {
            if ("BUY".equalsIgnoreCase(t.getSide())) {
                net = net.add(t.getQuantity());
                buyQty = buyQty.add(t.getQuantity());
                buyCost = buyCost.add(t.getQuantity().multiply(t.getPrice()));
            } else {
                net = net.subtract(t.getQuantity());
            }
        }

        BigDecimal avgPrice = buyQty.signum() == 0
                ? BigDecimal.ZERO
                : buyCost.divide(buyQty, SCALE, RoundingMode.HALF_UP);

        BigDecimal lastPrice = trades.stream()
                .max(Comparator.comparing(Trade::getTradeDate).thenComparing(Trade::getId))
                .map(Trade::getPrice)
                .orElse(BigDecimal.ZERO);

        BigDecimal marketValue = net.multiply(lastPrice).setScale(SCALE, RoundingMode.HALF_UP);

        Position position = positionRepo
                .findByAccountIdAndInstrumentId(accountId, instrumentId)
                .orElseGet(() -> {
                    Position p = new Position();
                    p.setAccountId(accountId);
                    p.setInstrumentId(instrumentId);
                    return p;
                });

        position.setNetQuantity(net.setScale(SCALE, RoundingMode.HALF_UP));
        position.setAvgPrice(avgPrice);
        position.setMarketValue(marketValue);
        position.setUpdatedAt(LocalDateTime.now());

        positionRepo.save(position);
    }
}

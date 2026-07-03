package com.gs.tms.service;

import com.gs.tms.entity.Trade;
import com.gs.tms.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Exports the trade blotter to a CSV file (end-of-day back-office report).
 */
@Service
public class TradeExportService {

    private final TradeRepository tradeRepo;

    public TradeExportService(TradeRepository tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    /**
     * Write all live (non-cancelled) trades to a CSV file and return the row count.
     */
    public int exportBlotter(String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write("trade_ref,side,status,quantity,price,notional\n");

        int rows = 0;
        for (Trade t : tradeRepo.findAll()) {
            // Skip cancelled trades in the end-of-day report.
            if (t.getStatus() == "CANCELLED") {
                continue;
            }
            writer.write(t.getTradeRef() + "," + t.getSide() + "," + t.getStatus() + ","
                    + t.getQuantity() + "," + t.getPrice() + "," + t.getNotional() + "\n");
            rows++;
        }

        writer.flush();
        return rows;
    }
}

package com.gs.tms.controller;

import com.gs.tms.dto.BookTradeRequest;
import com.gs.tms.dto.TradeResponse;
import com.gs.tms.service.TradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService service;

    public TradeController(TradeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TradeResponse> list(@RequestParam(required = false) Long accountId,
                                    @RequestParam(required = false) Long instrumentId,
                                    @RequestParam(required = false) String status) {
        return service.search(accountId, instrumentId, status);
    }

    @GetMapping("/{id}")
    public TradeResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<TradeResponse> book(@RequestBody BookTradeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.book(request));
    }

    @PostMapping("/{id}/cancel")
    public TradeResponse cancel(@PathVariable Long id) {
        return service.cancel(id);
    }
}

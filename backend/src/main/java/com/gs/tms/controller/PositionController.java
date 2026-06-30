package com.gs.tms.controller;

import com.gs.tms.entity.Position;
import com.gs.tms.service.PositionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService service;

    public PositionController(PositionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Position> list(@RequestParam(required = false) Long accountId) {
        if (accountId != null) {
            return service.findByAccount(accountId);
        }
        return service.findAll();
    }
}

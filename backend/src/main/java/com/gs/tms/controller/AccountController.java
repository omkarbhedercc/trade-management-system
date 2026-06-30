package com.gs.tms.controller;

import com.gs.tms.entity.Account;
import com.gs.tms.entity.Position;
import com.gs.tms.service.AccountService;
import com.gs.tms.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;
    private final PositionService positionService;

    public AccountController(AccountService service, PositionService positionService) {
        this.service = service;
        this.positionService = positionService;
    }

    @GetMapping
    public List<Account> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Account get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(account));
    }

    @PutMapping("/{id}")
    public Account update(@PathVariable Long id, @RequestBody Account account) {
        return service.update(id, account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/positions")
    public List<Position> positions(@PathVariable Long id) {
        // ensure the account exists (throws 404 otherwise)
        service.findById(id);
        return positionService.findByAccount(id);
    }
}

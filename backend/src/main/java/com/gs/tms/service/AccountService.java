package com.gs.tms.service;

import com.gs.tms.entity.Account;
import com.gs.tms.exception.ResourceNotFoundException;
import com.gs.tms.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    public List<Account> findAll() {
        return repo.findAll();
    }

    public Account findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    public Account create(Account account) {
        account.setId(null);
        return repo.save(account);
    }

    public Account update(Long id, Account changes) {
        Account existing = findById(id);
        existing.setAccountNumber(changes.getAccountNumber());
        existing.setClientName(changes.getClientName());
        existing.setAccountType(changes.getAccountType());
        existing.setBaseCurrency(changes.getBaseCurrency());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Account not found: " + id);
        }
        repo.deleteById(id);
    }
}

package com.gs.tms.service;

import com.gs.tms.entity.Instrument;
import com.gs.tms.exception.ResourceNotFoundException;
import com.gs.tms.repository.InstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentService {

    private final InstrumentRepository repo;

    public InstrumentService(InstrumentRepository repo) {
        this.repo = repo;
    }

    public List<Instrument> findAll() {
        return repo.findAll();
    }

    public Instrument findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found: " + id));
    }

    public Instrument create(Instrument instrument) {
        instrument.setId(null);
        return repo.save(instrument);
    }

    public Instrument update(Long id, Instrument changes) {
        Instrument existing = findById(id);
        existing.setTicker(changes.getTicker());
        existing.setName(changes.getName());
        existing.setAssetClass(changes.getAssetClass());
        existing.setCurrency(changes.getCurrency());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Instrument not found: " + id);
        }
        repo.deleteById(id);
    }
}

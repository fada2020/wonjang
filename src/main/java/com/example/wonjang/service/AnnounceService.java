package com.example.wonjang.service;

import com.example.wonjang.model.Announce;
import com.example.wonjang.repository.AnnounceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AnnounceService {
    private final AnnounceRepository announceRepository;

    public AnnounceService(AnnounceRepository announceRepository) {
        this.announceRepository = announceRepository;
    }

    public Page<Announce> findAll(Pageable pageable) {
        return announceRepository.findAll(pageable);
    }

    public Optional<Announce> findById(Integer id) {
        return announceRepository.findById(id);
    }
    //@CacheEvict(value = "pinCount", allEntries = true)
    public void save(Announce announce) {
        announceRepository.save(announce);
    }
    //@Cacheable("pinCount")
    public int countByPin(boolean pin) {
        return announceRepository.countByPin(pin);
    }

    public List<Announce> findByPin(boolean pin) {
        return announceRepository.findByPin(pin);
    }
}

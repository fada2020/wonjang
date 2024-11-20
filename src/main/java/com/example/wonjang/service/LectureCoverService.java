package com.example.wonjang.service;

import com.example.wonjang.model.LectureCover;
import com.example.wonjang.repository.LectureCoverRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class LectureCoverService {
    private final LectureCoverRepository lectureCoverRepository;

    public LectureCoverService(LectureCoverRepository lectureCoverRepository) {
        this.lectureCoverRepository = lectureCoverRepository;
    }

    public Page<LectureCover> findAll(Pageable pageable) {
        return lectureCoverRepository.findAll(pageable);
    }

    public Optional<LectureCover> findById(Long id) {
        return lectureCoverRepository.findById(id);
    }
}

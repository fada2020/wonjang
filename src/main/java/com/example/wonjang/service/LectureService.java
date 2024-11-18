package com.example.wonjang.service;

import com.example.wonjang.model.Lecture;
import com.example.wonjang.repository.LectureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Page<Lecture> findAll(Pageable pageable) {
        return lectureRepository.findAll(pageable);
    }

    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }
}

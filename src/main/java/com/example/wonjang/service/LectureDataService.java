package com.example.wonjang.service;

import com.example.wonjang.model.LectureData;
import com.example.wonjang.repository.LectureDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LectureDataService {
    private final LectureDataRepository lectureDataRepository;

    public LectureDataService(LectureDataRepository lectureDataRepository) {
        this.lectureDataRepository = lectureDataRepository;
    }

    public Page<LectureData> findAll(Pageable pageable) {
        return lectureDataRepository.findAll(pageable);
    }
}

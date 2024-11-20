package com.example.wonjang.repository;

import com.example.wonjang.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByLectureCoverId(Long id);
}

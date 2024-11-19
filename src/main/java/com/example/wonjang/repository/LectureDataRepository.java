package com.example.wonjang.repository;

import com.example.wonjang.model.LectureData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureDataRepository extends JpaRepository<LectureData, Integer> {
}

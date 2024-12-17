package com.example.wonjang.repository;

import com.example.wonjang.model.UserVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserVisitRepository extends JpaRepository<UserVisit, Integer> {
    Optional<UserVisit> findByLocalDate(LocalDate localDate);

    List<UserVisit> findByLocalDateBetween(LocalDate monday, LocalDate sunday);
}

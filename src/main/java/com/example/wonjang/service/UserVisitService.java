package com.example.wonjang.service;

import com.example.wonjang.model.UserVisit;
import com.example.wonjang.repository.UserVisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserVisitService {
    private final UserVisitRepository userVisitRepository;

    public UserVisitService(UserVisitRepository userVisitRepository) {
        this.userVisitRepository = userVisitRepository;
    }

    public Optional<UserVisit> findByLocalDate(LocalDate localDate) {
        return userVisitRepository.findByLocalDate(localDate);
    }

    public void save(UserVisit userVisit) {
        userVisitRepository.save(userVisit);
    }
    public Map<DayOfWeek, Long> getThisWeekVisits() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        return userVisitRepository.findByLocalDateBetween(monday, sunday).stream()
                .collect(Collectors.groupingBy(
                        visit -> visit.getLocalDate().getDayOfWeek(),
                        Collectors.counting()
                ));
    }
}

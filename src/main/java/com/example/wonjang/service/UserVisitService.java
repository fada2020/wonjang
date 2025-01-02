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
    public Map<DayOfWeek, Integer> getThisWeekVisits() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        // 월요일부터 일요일까지의 날짜 범위에 해당하는 방문 기록을 가져옴
        List<UserVisit> visits = userVisitRepository.findByLocalDateBetween(monday, sunday);

        Map<DayOfWeek, Integer> visitCounts = new HashMap<>();

        // 월요일부터 일요일까지 날짜별로 접속자 수 집계
        for (int i = 0; i < 7; i++) {
            // 월요일부터 시작하여 7일 동안 반복
            LocalDate date = monday.plusDays(i);  // 월요일을 기준으로 +1, +2, ..., +6 일 계산
            DayOfWeek dayOfWeek = date.getDayOfWeek();  // 해당 날짜의 요일을 가져옴
            int visitCount = (int) visits.stream()
                    .filter(visit -> visit.getLocalDate().equals(date))
                    .count();
            visitCounts.put(dayOfWeek, visitCount);
        }

        return visitCounts;
    }
}

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
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            /*
            * with() 메서드는 LocalDate 객체를 특정 TemporalField(이 경우 DayOfWeek)를 기준으로 변경하는 메서드
            * 이 메서드는 monday 날짜를 기준으로, 원하는 요일에 해당하는 날짜를 반환
            * */
            LocalDate date = monday.with(DayOfWeek.of(dayOfWeek.getValue()));
            int visitCount = (int) visits.stream()
                    .filter(visit -> visit.getLocalDate().equals(date))
                    .count();
            visitCounts.put(dayOfWeek, visitCount);
        }
        return visitCounts;
    }
}

package com.example.wonjang.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class ScheduleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String subject; // 예: "수학", "영어"
    private String teacher; // 강사 이름
    private LocalTime startTime; // 시작 시간 (LocalDateTime 대신 LocalTime)
    private LocalTime endTime;   // 종료 시간
    private Boolean isEnd;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
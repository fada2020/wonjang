package com.example.wonjang.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title; // 예: "봄 학기 시간표"
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isEnd;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleDetail> details = new ArrayList<>();
}
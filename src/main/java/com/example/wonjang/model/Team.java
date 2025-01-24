package com.example.wonjang.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; // 팀 이름
    private String description; // 팀 설명 (예: "고등부 A팀", "중등부 B팀")

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<ScheduleDetail> scheduleDetails = new ArrayList<>();
}
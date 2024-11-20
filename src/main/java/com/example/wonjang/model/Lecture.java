package com.example.wonjang.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Lecture extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String imgUrl;
    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();
}

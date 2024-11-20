package com.example.wonjang.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class LectureCover extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    private String imgUrl;
    private String type;
    private String target;
    @OneToMany(mappedBy = "lectureCover", fetch = FetchType.LAZY)
    private List<Lecture> lectures = new ArrayList<>();
}

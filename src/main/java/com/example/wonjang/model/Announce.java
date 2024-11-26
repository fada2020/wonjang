package com.example.wonjang.model;

import jakarta.persistence.*;

@Entity
public class Announce extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(columnDefinition = "text")
    private String content;
}

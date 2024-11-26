package com.example.wonjang.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Entity
public class Announce extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(columnDefinition = "text")
    private String content;

    public void update(Announce newAnnounce) {
        this.title = newAnnounce.getTitle();
        this.content = newAnnounce.getContent();
    }
}

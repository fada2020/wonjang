package com.example.wonjang.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@Builder
@Entity
public class UserVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Builder.Default
    private Integer connect = 1;
    @Builder.Default
    @Column(unique = true)
    private LocalDate localDate = LocalDate.now();

    @PrePersist
    public void onPrePersist() {
        // 방문 날짜 저장
        this.localDate = LocalDate.now();
        if(this.connect == null)this.connect = 1;
    }

    public UserVisit(Integer id, Integer connect, LocalDate localDate) {
        this.id = id;
        this.connect = connect;
        this.localDate = localDate;
    }

    public UserVisit() {
    }

    public void plusConnect() {
        this.connect = this.connect == null ? 1 : this.connect + 1;
    }
}

package com.example.wonjang.model;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

@Entity
public class RegisterCode extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String expiredAt;
    private String auth;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래 키 설정
    private Member member;
}

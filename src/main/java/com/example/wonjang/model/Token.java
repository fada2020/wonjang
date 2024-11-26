package com.example.wonjang.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Token extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accessToken;
    private String refreshToken;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Token() {
    }
    @Builder
    public Token(String accessToken, String refreshToken, Member member) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public void updateMember(Member member) {
        this.member = member;
    }
}
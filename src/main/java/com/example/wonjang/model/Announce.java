package com.example.wonjang.model;

import com.example.wonjang.dto.AnnounceDto;
import jakarta.persistence.*;
import lombok.Builder;
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
    @Builder.Default
    private Boolean pin = Boolean.FALSE;
    @Builder
    public Announce(Integer id, String title, String content, Boolean pin) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pin = pin;
    }

    public Announce() {
    }

    public void update(Announce newAnnounce) {
        this.title = newAnnounce.getTitle();
        this.content = newAnnounce.getContent();
    }
    public AnnounceDto toDto(){
        return AnnounceDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .pin(this.pin)
                .build();
    }
}

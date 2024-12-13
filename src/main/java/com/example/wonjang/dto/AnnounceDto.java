package com.example.wonjang.dto;

import com.example.wonjang.model.Announce;
import lombok.Builder;
import lombok.Data;

@Data
public class AnnounceDto {
    private Integer id;
    private String title;
    private String content;
    @Builder
    public AnnounceDto(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public AnnounceDto() {
    }

    public Announce toEntity(){
        return Announce.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}

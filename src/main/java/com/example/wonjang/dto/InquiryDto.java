package com.example.wonjang.dto;

import com.example.wonjang.model.Inquiry;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InquiryDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public InquiryDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public InquiryDto() {
    }


    public Inquiry toEntity() {
        return Inquiry.builder().build();
    }
}

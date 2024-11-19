package com.example.wonjang.dto;

import com.example.wonjang.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class SignUpDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    private String confirmPassword;
    @NotBlank
    private String degree;
    private String grade;

    @Builder
    public SignUpDto(String email, String name, String password, String confirmPassword, String degree, String grade) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.degree = degree;
        this.grade = grade;
    }

    public SignUpDto() {
    }
    public void encodePassword(String encodedPassword){
        this.password = encodedPassword;
    }
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .degree(degree)
                .grade(grade)
                .build();
    }
}

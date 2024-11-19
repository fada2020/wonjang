package com.example.wonjang.dto;

import com.example.wonjang.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginDto() {
    }


}

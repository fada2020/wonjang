package com.example.wonjang.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private String email;
    private String name;
    private String token;
    @Builder
    public UserDto(String email, String name, String token) {
        this.email = email;
        this.name = name;
        this.token = token;
    }

    public UserDto() {
    }
}

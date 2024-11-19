package com.example.wonjang.dto;

import com.example.wonjang.model.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private String email;
    private String name;
    private Role role;
    @Builder
    public UserDto(String email, String name, Role role) {
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public UserDto() {
    }
}

package com.example.wonjang.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter @Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateAdminDto {
    private String email;
    @NotBlank
    private String name;
    private String password;
    private MultipartFile pictureFile;
    private String picture;
    private String mobile;



}

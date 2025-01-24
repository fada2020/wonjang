package com.example.wonjang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DayDto {
    private String date;      // '23' 같은 일자
    private String fullDate;  // '2025-01-23' 같은 전체 날짜
}

package com.example.wonjang.controller;

import com.example.wonjang.model.Feedback;
import com.example.wonjang.service.LectureService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/api/v1")
@RestController
public class ApiV1Controller {
    private final LectureService lectureService;

    public ApiV1Controller(LectureService lectureService) {
        this.lectureService = lectureService;
    }
    @PostMapping("/feedback")
    public String feedback(
            @Valid @ModelAttribute("feedback") Feedback feedback

    ) {

    }
}

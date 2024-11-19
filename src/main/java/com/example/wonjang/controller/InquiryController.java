package com.example.wonjang.controller;

import com.example.wonjang.model.Lecture;
import com.example.wonjang.service.LectureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@RequestMapping("/inquiry")
@Controller
public class InquiryController {
    private final LectureService lectureService;

    public InquiryController(LectureService lectureService) {
        this.lectureService = lectureService;
    }
    @GetMapping("")
    public String inquiryIndex(){
        return "user/inquiry/index";
    }
}

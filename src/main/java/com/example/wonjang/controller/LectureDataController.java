package com.example.wonjang.controller;

import com.example.wonjang.model.LectureData;
import com.example.wonjang.service.LectureDataService;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/data")
@Controller
public class LectureDataController {
    private final LectureDataService lectureDataService;

    public LectureDataController(LectureDataService lectureDataService) {
        this.lectureDataService = lectureDataService;
    }

    @GetMapping("")
    public String dataIndex(
            Model model
            , @PageableDefault(size = 10) @SortDefault.SortDefaults(@SortDefault(sort="id", direction = Sort.Direction.DESC)) Pageable pageable){
        Page<LectureData>lectureDataPage = lectureDataService.findAll(pageable);
        model.addAttribute("lectureData", lectureDataPage);
        return "user/data/index";
    }
}

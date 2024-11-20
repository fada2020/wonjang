package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.UserDto;
import com.example.wonjang.model.Lecture;
import com.example.wonjang.model.LectureCover;
import com.example.wonjang.service.LectureCoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.wonjang.service.LectureService;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/lecture")
@Controller
public class LectureController {
    private final LectureService lectureService;
    private final LectureCoverService lectureCoverService;

    public LectureController(LectureService lectureService, LectureCoverService lectureCoverService) {
        this.lectureService = lectureService;
        this.lectureCoverService = lectureCoverService;
    }
    @GetMapping("")
    public String lectureIndex(
            Model model
            , @PageableDefault(size = 10) @SortDefault.SortDefaults(@SortDefault(sort="id", direction = Sort.Direction.DESC)) Pageable pageable){
        Page<LectureCover>lectureCovers = lectureCoverService.findAll(pageable);
        model.addAttribute("lectureCovers", lectureCovers);
        return "user/lecture/list";
    }

    @GetMapping("/info/{id}")
    public String lectureInfo(
            @PathVariable Long id
            , Model model
            ){
        Optional<LectureCover>optionalLectureCover = lectureCoverService.findById(id);
        optionalLectureCover.ifPresent(lectureCover -> {
            model.addAttribute("lectureCover", lectureCover);
            model.addAttribute("lectures", lectureCover.getLectures());
        });
        return "user/lecture/index";
    }
    @GetMapping("/{videoId}")
    public String lectureIndex(
             @PathVariable("videoId") Long videoId
            , @SessionAttribute(value = "admin", required = false) UserDto admin
            , @SessionAttribute(value = "user", required = false) UserDto user
            , Model model
    ){
        if (admin == null && user == null) {
            return "redirect:/login";
        }
        Optional<Lecture>optionalLecture = lectureService.findById(videoId);
        if(optionalLecture.isPresent()) {
            model.addAttribute("lecture", optionalLecture.get());
            return "user/lecture/player";
        } else {
            return "redirect:/lecture";
        }
    }
}

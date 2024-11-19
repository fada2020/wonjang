package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.UserDto;
import com.example.wonjang.model.Lecture;
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

import java.util.Optional;

@Slf4j
@RequestMapping("/lecture")
@Controller
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }
    @GetMapping("")
    public String lectureIndex(
            Model model
            , @PageableDefault(size = 10) @SortDefault.SortDefaults(@SortDefault(sort="id", direction = Sort.Direction.DESC)) Pageable pageable){
        Page<Lecture>lectures = lectureService.findAll(pageable);
        model.addAttribute("lectures", lectures);
        return "user/lecture/index";
    }
    @GetMapping("/{id}")
    public String lectureIndex(
            @PathVariable Long id
            , @SessionAttribute(value = "admin", required = false) UserDto admin
            , @SessionAttribute(value = "user", required = false) UserDto user
            , Model model
    ){
        if (admin == null || user == null) {
            return "redirect:/login";
        }
        Optional<Lecture>optionalLecture = lectureService.findById(id);
        if(optionalLecture.isPresent()) {
            model.addAttribute("lecture", optionalLecture.get());
            return "user/lecture/player";
        } else {
            return "redirect:/lecture";
        }
    }
}

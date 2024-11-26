package com.example.wonjang.controller;

import com.example.wonjang.service.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/academy")
@Controller
public class AcademyController {
    private final InquiryService inquiryService;

    public AcademyController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping("/introduction")
    public String introduction(
    ){
        return "user/academy/introduction";
    }
    @GetMapping( "/teachers")
    public String teachers(
    ){
        return "user/academy/teachers";
    }
}

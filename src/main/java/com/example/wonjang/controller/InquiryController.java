package com.example.wonjang.controller;

import com.example.wonjang.dto.InquiryDto;
import com.example.wonjang.dto.UserDto;
import com.example.wonjang.model.Inquiry;
import com.example.wonjang.model.Lecture;
import com.example.wonjang.service.InquiryService;
import com.example.wonjang.service.LectureService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@RequestMapping("/inquiry")
@Controller
public class InquiryController {
    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }
    @GetMapping("")
    public String inquiryIndex(
            Model model
            , @SessionAttribute("user") UserDto userDto
    ){
        model.addAttribute("inquiryDto", new InquiryDto());
        return "user/inquiry/index";
    }

    @PostMapping("")
    public String inquiryIndex(
            Model model
            , @SessionAttribute("user") UserDto userDto
            , @Valid @ModelAttribute("inquiryDto") InquiryDto inquiryDto
            , BindingResult bindingResult
            ,  RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            model.addAttribute("inquiryDto", inquiryDto);
            return "user/inquiry/index";
        }
        Inquiry inquiry = inquiryDto.toEntity();
        inquiryService.save(inquiry);
        redirectAttributes.addFlashAttribute("message", "Success");
        return "redirect:/inquiry";
    }

}

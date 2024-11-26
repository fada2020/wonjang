package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.InquiryDto;
import com.example.wonjang.dto.UserDto;
import com.example.wonjang.model.Inquiry;
import com.example.wonjang.model.Member;
import com.example.wonjang.service.InquiryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/service")
@Controller
public class ServiceController {
    private final InquiryService inquiryService;

    public ServiceController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }
    @GetMapping("/terms")
    public String termsIndex(
            Model model
    ){
        return "service/terms";
    }
    @GetMapping("/terms/private")
    public String termsPrivate(
            Model model
    ){
        return "service/private";
    }
    @GetMapping("/faq")
    public String faqIndex(
            Model model
    ){
        return "service/faq";
    }
    @GetMapping("/use")
    public String useIndex(
            Model model
    ){
        return "service/use";
    }
    @GetMapping("/inquiry")
    public String inquiryIndex(
            Model model
            , @CurrentUser Member member
    ){
        model.addAttribute("inquiryDto", new InquiryDto());
        return "service/inquiry";
    }

    @PostMapping("/inquiry")
    public String inquiryIndex(
            Model model
            , @CurrentUser Member member
            , @Valid @ModelAttribute("inquiryDto") InquiryDto inquiryDto
            , BindingResult bindingResult
            ,  RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            model.addAttribute("inquiryDto", inquiryDto);
            return "service/inquiry";
        }
        Inquiry inquiry = inquiryDto.toEntity();
        inquiryService.save(inquiry);
        redirectAttributes.addFlashAttribute("message", "Success");
        return "redirect:/service/inquiry";
    }
    @GetMapping("/announce")
    public String announceIndex(){
        return "service/announce";
    }

    @GetMapping("/crew")
    public String crew(){
        return "service/crew";
    }
}

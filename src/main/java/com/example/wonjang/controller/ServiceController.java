package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.InquiryDto;
import com.example.wonjang.model.Announce;
import com.example.wonjang.model.Inquiry;
import com.example.wonjang.model.Member;
import com.example.wonjang.service.AnnounceService;
import com.example.wonjang.service.InquiryService;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/service")
@Controller
public class ServiceController {
    private final InquiryService inquiryService;
    private final AnnounceService announceService;

    public ServiceController(InquiryService inquiryService, AnnounceService announceService) {
        this.inquiryService = inquiryService;
        this.announceService = announceService;
    }
    @GetMapping("/terms")
    public String termsIndex(
            Model model
    ){
        return "user/service/terms";
    }
    @GetMapping("/terms/private")
    public String termsPrivate(
            Model model
    ){
        return "user/service/private";
    }
    @GetMapping("/faq")
    public String faqIndex(
            Model model
    ){
        return "user/service/faq";
    }
    @GetMapping("/use")
    public String useIndex(
            Model model
    ){
        return "user/service/use";
    }
    @GetMapping("/inquiry")
    public String inquiryIndex(
            Model model
            , @CurrentUser Member member
    ){
        model.addAttribute("inquiryDto", new InquiryDto());
        return "user/service/inquiry";
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
            return "user/service/inquiry";
        }
        Inquiry inquiry = inquiryDto.toEntity();
        inquiryService.save(inquiry);
        redirectAttributes.addFlashAttribute("message", "Success");
        return "redirect:/service/inquiry";
    }
    @GetMapping("/announce")
    public String announceIndex(
            Model model
           , @PageableDefault(size = 10) @SortDefault.SortDefaults({@SortDefault(sort="pin", direction = Sort.Direction.DESC)
           ,@SortDefault(sort="id", direction = Sort.Direction.DESC)}) Pageable pageable){
        Page<Announce> announcePage = announceService.findAll(pageable);
        model.addAttribute("announcePage", announcePage);
        return "user/service/announce";
    }
    @GetMapping("/announce/{id}")
    public String announceView(
            Model model
            , @PathVariable("id") Integer id){
        Optional<Announce> optionalAnnounce = announceService.findById(id);
        if(optionalAnnounce.isPresent()) {
            model.addAttribute("announceDto", optionalAnnounce.get().toDto());
            return "user/service/announceView";
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/crew")
    public String crew(){
        return "user/service/crew";
    }
}

package com.example.wonjang.controller;

import com.example.wonjang.dto.LoginDto;
import com.example.wonjang.dto.UserDto;
import com.example.wonjang.model.Member;
import com.example.wonjang.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;
    public AdminController(MemberService memberService, BCryptPasswordEncoder encoder) {
        this.memberService = memberService;
        this.encoder = encoder;
    }

    @GetMapping("")
    public String index(
            Model model
            , @SessionAttribute(value = "admin", required = false) UserDto userDto
    ){
        if (userDto == null) {
            model.addAttribute("loginDto", new LoginDto());
            return "admin/login";

        } else {
            return "admin/index";
        }
    }
    @GetMapping("/forgot")
    public String forgot(
            Model model
    ){
            return "admin/forgot";
    }

    @PostMapping("")
    public String loginProcess(
            Model model
            , HttpSession session
            , @Valid @ModelAttribute("loginDto") LoginDto loginDto
            , BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginDto", loginDto);
            return "admin/login";
        }
        Optional<Member> optionalMember = memberService.findByEmail(loginDto.getEmail());
        if(optionalMember.isEmpty()) {
            bindingResult.addError(new FieldError("loginDto","email", "이메일 또는 비밀번호를 확인해주세요."));
            model.addAttribute("loginDto", loginDto);
            return "admin/login";
        }
        Member member = optionalMember.get();
        String password = member.getPassword();
        if(!encoder.matches(loginDto.getPassword(), password)) {
            bindingResult.addError(new FieldError("loginDto","email", "이메일 또는 비밀번호를 확인해주세요."));
            model.addAttribute("loginDto", loginDto);
            return "admin/login";
        }
        session.setAttribute("admin", member.toDto());
      return "admin/index";
    }
}

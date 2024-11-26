package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
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
            , @CurrentUser Member member
    ){
        return "admin/index";
    }
    @GetMapping("/forgot")
    public String forgot(
            Model model
    ){
            return "admin/forgot";
    }
}

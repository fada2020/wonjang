package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.UpdateAdminDto;
import com.example.wonjang.dto.UpdateMemberDto;
import com.example.wonjang.model.Member;
import com.example.wonjang.service.MemberService;
import com.example.wonjang.utils.FileUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;
    private final FileUtil fileUtil;
    public AdminController(MemberService memberService, BCryptPasswordEncoder encoder, FileUtil fileUtil) {
        this.memberService = memberService;
        this.encoder = encoder;
        this.fileUtil = fileUtil;
    }

    @GetMapping("")
    public String index(
            Model model
            , @CurrentUser Member member
    ){
        return "admin/index";
    }

    @GetMapping("/mypage")
    public String mypage(
            Model model
            , @CurrentUser Member member
    ){
        model.addAttribute("member", member.toSignUpDto());
        List<Integer> yearsByDegree = memberService.getYearsByDegree(member.getDegree());
        System.out.println("yearsByDegree = " + yearsByDegree);
        model.addAttribute("yearsByDegree", yearsByDegree);
        model.addAttribute("updateAdminDto", new UpdateAdminDto() );
        return "admin/mypage";
    }
    @PostMapping(value = "/mypage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String updateMember(
            Model model
            , @CurrentUser Member member
            , @Valid @ModelAttribute("updateMemberDto") UpdateAdminDto updateAdminDto
            , BindingResult bindingResult
    ) throws Exception {
        if (bindingResult.hasErrors()) {
            model.addAttribute("updateAdminDto", updateAdminDto);
            model.addAttribute("member", member.toSignUpDto());
            return "admin/mypage";
        }
        if (StringUtils.isNotEmpty(updateAdminDto.getPassword())) {
            String encoded = encoder.encode(updateAdminDto.getPassword());
            updateAdminDto.setPassword(encoded);
        }

        if(updateAdminDto.getPictureFile() != null) {
            String path = fileUtil.saveFile(updateAdminDto.getPictureFile(), member.getId().toString());
            member.updatePicture(path);
        }
        member.updateMember(updateAdminDto);
        memberService.save(member);
        return "redirect:/admin/mypage";
    }
    @GetMapping("/service/announce")
    public String announceIndex(
            Model model
            , @CurrentUser Member member
    ){
        return "admin/announce/index";
    }

    @GetMapping("/forgot")
    public String forgot(
            Model model
    ){
            return "admin/forgot";
    }
}

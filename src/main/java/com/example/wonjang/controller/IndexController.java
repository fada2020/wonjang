package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.AnnounceDto;
import com.example.wonjang.dto.LoginDto;
import com.example.wonjang.dto.SignUpDto;
import com.example.wonjang.dto.UpdateMemberDto;
import com.example.wonjang.model.Announce;
import com.example.wonjang.model.Member;
import com.example.wonjang.service.AnnounceService;
import com.example.wonjang.service.MemberService;
import com.example.wonjang.utils.FileUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
public class IndexController {
    private final MemberService memberService;
    private final AnnounceService announceService;
    private final FileUtil fileUtil;

    private final BCryptPasswordEncoder encoder;
    public IndexController(MemberService memberService, AnnounceService announceService, FileUtil fileUtil) {
        this.memberService = memberService;
        this.announceService = announceService;
        this.fileUtil = fileUtil;
        this.encoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/")
    public String index(Model model){
        List<Announce>announceList = announceService.findByPin(true);
        List<AnnounceDto> announceDtos = announceList.stream().map(Announce::toDto).sorted(Comparator.comparing(AnnounceDto::getId).reversed()).limit(5).toList();
        model.addAttribute("announceDtos", announceDtos);
        return "user/index";
    }
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginDto", new LoginDto());
        return "user/login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("signUpDto", new SignUpDto());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signupProcess(
            Model model
            , @Valid @ModelAttribute("signUpDto") SignUpDto signUpDto
            , BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            model.addAttribute("signUpDto", signUpDto);
            return "user/signup";
        }
        String password = signUpDto.getPassword();
        signUpDto.encodePassword(encoder.encode(password));
        Member member = signUpDto.toEntity();
        memberService.save(member);
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String mypage(Model model
            , @CurrentUser Member member
    ){
        model.addAttribute("member", member.toSignUpDto());
        List<Integer> yearsByDegree = memberService.getYearsByDegree(member.getDegree());
        System.out.println("yearsByDegree = " + yearsByDegree);
        model.addAttribute("yearsByDegree", yearsByDegree);
        model.addAttribute("updateMemberDto", new UpdateMemberDto() );
        return "user/mypage";
    }

    @PostMapping(value = "/mypage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String updateMember(
            Model model
            , @CurrentUser Member member
        , @Valid @ModelAttribute("updateMemberDto") UpdateMemberDto updateMemberDto
        , BindingResult bindingResult
    ) throws Exception {
        if (bindingResult.hasErrors()) {
            model.addAttribute("updateMemberDto", updateMemberDto);
            model.addAttribute("member", member.toSignUpDto());
            return "user/mypage";
        }
        if (StringUtils.isNotEmpty(updateMemberDto.getPassword())) {
            String encoded = encoder.encode(updateMemberDto.getPassword());
            updateMemberDto.setPassword(encoded);
        }

        if(updateMemberDto.getPictureFile() != null) {
            String path = fileUtil.saveFile(updateMemberDto.getPictureFile(), member.getId().toString());
            member.updatePicture(path);
        }
        member.updateMember(updateMemberDto);
        memberService.save(member);
        return "redirect:/mypage";
    }
}

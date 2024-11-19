package com.example.wonjang.controller;

import com.example.wonjang.dto.LoginDto;
import com.example.wonjang.dto.SignUpDto;
import com.example.wonjang.dto.UpdateMemberDto;
import com.example.wonjang.dto.UserDto;
import com.example.wonjang.model.Member;
import com.example.wonjang.service.MemberService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Slf4j
@Controller
public class IndexController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;
    public IndexController(MemberService memberService) {
        this.memberService = memberService;
        this.encoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/")
    public String index(){
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
    @PostMapping("/login")
    public String loginProcess(
            Model model
            , HttpSession session
            , @Valid @ModelAttribute("loginDto") LoginDto loginDto
            , BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginDto", loginDto);
            return "user/login";
        }
        Optional<Member>optionalMember = memberService.findByEmail(loginDto.getEmail());
        if(optionalMember.isEmpty()) {
            bindingResult.addError(new FieldError("loginDto","email", "이메일 또는 비밀번호를 확인해주세요."));
            model.addAttribute("loginDto", loginDto);
            return "user/login";
        }
        Member member = optionalMember.get();
        String password = member.getPassword();
        if(!encoder.matches(loginDto.getPassword(), password)) {
            bindingResult.addError(new FieldError("loginDto","email", "이메일 또는 비밀번호를 확인해주세요."));
            model.addAttribute("loginDto", loginDto);
            return "user/login";
        }
        session.setAttribute("user", member.toDto() );
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
    , @SessionAttribute("user") UserDto userDto){
        Optional<Member>optionalMember = memberService.findByEmail(userDto.getEmail());
        if(optionalMember.isEmpty()) return "redirect:/";
        Member member = optionalMember.get();
        model.addAttribute("member", member.toSignUpDto());
        model.addAttribute("updateMemberDto", new UpdateMemberDto() );
        return "user/mypage";
    }
    @PostMapping(value = "/mypage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String updateMember(
            Model model
            , @SessionAttribute("user") UserDto userDto
        , @Valid @ModelAttribute("updateMemberDto") UpdateMemberDto updateMemberDto
        , BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            model.addAttribute("updateMemberDto", updateMemberDto);
            return "user/mypage";
        }
        if (StringUtils.isNotEmpty(updateMemberDto.getPassword())) {
            String encoded = encoder.encode(updateMemberDto.getPassword());
            updateMemberDto.setPassword(encoded);
        }
        Optional<Member> optionalMember = memberService.findByEmail(userDto.getEmail());
        if(optionalMember.isEmpty()) return "redirect:/";
        Member member = optionalMember.get();
        if(updateMemberDto.getPicture() != null) {

        }
        member.updateMember(updateMemberDto);
        System.out.println("updateMemberDto = " + updateMemberDto);
        model.addAttribute("updateMemberDto", new UpdateMemberDto() );
        return "redirect:/mypage";
    }
}

package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.UpdateAdminDto;
import com.example.wonjang.model.Announce;
import com.example.wonjang.model.Member;
import com.example.wonjang.service.AnnounceService;
import com.example.wonjang.service.MemberService;
import com.example.wonjang.utils.FileUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MemberService memberService;
    private final AnnounceService announceService;
    private final BCryptPasswordEncoder encoder;
    private final FileUtil fileUtil;
    public AdminController(MemberService memberService, AnnounceService announceService, BCryptPasswordEncoder encoder, FileUtil fileUtil) {
        this.memberService = memberService;
        this.announceService = announceService;
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
    @GetMapping("/announce/list")
    public String announceIndex(
            Model model
            , @CurrentUser Member member
            , @PageableDefault(size = 10) @SortDefault.SortDefaults(@SortDefault(sort="id", direction = Sort.Direction.DESC)) Pageable pageable){
        Page<Announce> announcePage = announceService.findAll(pageable);  // 공지사항 목록을 가져오는 서비스 메소드
        model.addAttribute("announcePage", announcePage);
        return "admin/announce/list";
    }
    @GetMapping(value={"/announce/{id}", "/announce"})
    public String announceIndex(
            Model model
            , @PathVariable(value="id", required = false) Integer id
            , @CurrentUser Member member
    ){
        if (id != null) {
            Optional<Announce> announceOptional = announceService.findById(id);
            announceOptional.ifPresent(announce -> model.addAttribute("announce", announce));
        } else {
            model.addAttribute("announce", new Announce());
        }
        return "admin/announce/update";

    }
    @PostMapping(value={"/announce/{id}", "/announce"})
    public String announceProcess(
            Model model
            , @PathVariable(value="id", required = false) Integer id
            , @CurrentUser Member member
            , @ModelAttribute("announce") Announce newAnnounce
    ){
        System.out.println("newAnnounce = " + newAnnounce);
        if (id != null) {
            Optional<Announce> announceOptional = announceService.findById(id);
            announceOptional.ifPresent(announce ->{
                announce.update(newAnnounce);
                announceService.save(announce);
            });
        } else {
            announceService.save(newAnnounce);
        }
        return "admin/announce/update";

    }
    @GetMapping("/forgot")
    public String forgot(
            Model model
    ){
            return "admin/forgot";
    }
}

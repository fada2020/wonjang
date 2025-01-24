package com.example.wonjang.controller;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.dto.AnnounceDto;
import com.example.wonjang.dto.DayDto;
import com.example.wonjang.dto.UpdateAdminDto;
import com.example.wonjang.model.Announce;
import com.example.wonjang.model.Member;
import com.example.wonjang.model.Schedule;
import com.example.wonjang.model.UserVisit;
import com.example.wonjang.service.AnnounceService;
import com.example.wonjang.service.MemberService;
import com.example.wonjang.service.ScheduleService;
import com.example.wonjang.service.UserVisitService;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MemberService memberService;
    private final AnnounceService announceService;
    private final ScheduleService scheduleService;
    private final UserVisitService userVisitService;
    private final BCryptPasswordEncoder encoder;
    private final FileUtil fileUtil;
    public AdminController(MemberService memberService, AnnounceService announceService, ScheduleService scheduleService, UserVisitService userVisitService, BCryptPasswordEncoder encoder, FileUtil fileUtil) {
        this.memberService = memberService;
        this.announceService = announceService;
        this.scheduleService = scheduleService;
        this.userVisitService = userVisitService;
        this.encoder = encoder;
        this.fileUtil = fileUtil;
    }
    public List<List<DayDto>> generateCalendar() {
        List<List<DayDto>> calendar = new ArrayList<>();
        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());

        // 해당 월의 첫째 날과 마지막 날 가져오기
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // 첫 주의 시작(일요일) 구하기
        LocalDate startOfCalendar = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        // 마지막 주의 끝(토요일) 구하기
        LocalDate endOfCalendar = lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        // 현재 주 리스트 초기화
        List<DayDto> currentWeek = new ArrayList<>();
        for (LocalDate date = startOfCalendar; !date.isAfter(endOfCalendar); date = date.plusDays(1)) {
            if (date.getMonthValue() == today.getMonthValue()) {
                // 현재 월의 날짜
                currentWeek.add(new DayDto(String.valueOf(date.getDayOfMonth()), date.toString()));
            } else {
                // 이전/다음 월의 날짜는 비워둠
                currentWeek.add(new DayDto("", ""));
            }

            // 주 단위로 끊기
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                calendar.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }
        return calendar;
    }

    @GetMapping("")
    public String index(
            Model model
            , @CurrentUser Member member
    ){
        List<UserVisit> thisWeekVisits = userVisitService.getThisWeekVisits();
        model.addAttribute("thisWeekVisits",thisWeekVisits);
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
            announceOptional.ifPresent(announce -> model.addAttribute("announceDto", announce.toDto()));
        } else {
            model.addAttribute("announceDto", new AnnounceDto());
        }
        return "admin/announce/update";

    }
    @PostMapping(value={"/announce/{id}", "/announce"})
    public String announceProcess(
            Model model
            , @PathVariable(value="id", required = false) Integer id
            , @CurrentUser Member member
            , @ModelAttribute("announceDto") AnnounceDto announceDto
    ){
        System.out.println("announceDto = " + announceDto);
        if (id != null) {
            Optional<Announce> announceOptional = announceService.findById(id);
            announceOptional.ifPresent(announce ->{
                announce.update(announceDto.toEntity());
                announceService.save(announce);
            });
        } else {
            announceService.save(announceDto.toEntity());
        }
        return "admin/announce/update";

    }
    @GetMapping("/forgot")
    public String forgot(
            Model model
    ){
            return "admin/forgot";
    }

    @GetMapping("/data/list")
    public String dataIndex(
            Model model
            , @CurrentUser Member member
            , @PageableDefault(size = 10) @SortDefault.SortDefaults(@SortDefault(sort="id", direction = Sort.Direction.DESC)) Pageable pageable){
        Page<Announce> announcePage = announceService.findAll(pageable);  // 공지사항 목록을 가져오는 서비스 메소드
        model.addAttribute("announcePage", announcePage);
        return "admin/data/list";
    }
    @GetMapping("/schedule")
    public String scheduleIndex(
            Model model
            , @CurrentUser Member member
            , @PageableDefault(size = 10) @SortDefault.SortDefaults(@SortDefault(sort="id", direction = Sort.Direction.DESC)) Pageable pageable){
        Page<Schedule>schedulePage = scheduleService.findAll(pageable);
        model.addAttribute("schedulePage", schedulePage);
        model.addAttribute("calendar", generateCalendar());
        return "admin/schedule/index";
    }
}

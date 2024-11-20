package com.example.wonjang.service;

import com.example.wonjang.model.Member;
import com.example.wonjang.repository.MemberRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    public void save(Member member){
        memberRepository.save(member);
    }

    public Optional<Member> findByEmail(@Email @NotBlank String email) {
        return memberRepository.findByEmail(email);
    }
    public List<Integer> getYearsByDegree(String degree) {
        return switch (degree) {
            case "elementary" -> Arrays.asList(1, 2, 3, 4, 5, 6);
            case "middle", "high" -> Arrays.asList(1, 2, 3);
            case "university" -> Arrays.asList(1, 2, 3, 4);
            default -> Collections.emptyList();
        };
    }
}

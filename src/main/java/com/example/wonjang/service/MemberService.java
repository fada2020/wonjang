package com.example.wonjang.service;

import com.example.wonjang.exception.CustomException;
import com.example.wonjang.model.Member;
import com.example.wonjang.repository.MemberRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.wonjang.exception.ErrorCode.NOT_FOUND_USER;

@Slf4j
@Service
public class MemberService  implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("로그인 시작={}", email);
        return  memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER, "없는 회원"));

    }
}

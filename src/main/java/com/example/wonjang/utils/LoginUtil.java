package com.example.wonjang.utils;

import com.example.wonjang.model.Member;
import com.example.wonjang.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class LoginUtil {
    @Autowired
    MemberRepository memberRepository;

    public Object getPrincipal(Authentication authentication){

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Member) {
            return principal;
        }

        return null;
    }


}

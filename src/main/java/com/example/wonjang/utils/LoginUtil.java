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
            System.out.println(" = 없다 " );
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Member) {
            System.out.println(" = 있다 " );

            return principal;
        }

        return null;
    }
    public boolean isEnabled(Authentication authentication){
        Member principal = (Member)getPrincipal(authentication);
        return principal != null && principal.isEnabled();
    }

}

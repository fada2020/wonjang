package com.example.wonjang.handler;

import com.example.wonjang.model.Member;
import com.example.wonjang.repository.TokenRepositoy;
import com.example.wonjang.utils.CookieUtil;
import com.example.wonjang.utils.JwtUtil;
import com.example.wonjang.utils.LoginUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.wonjang.utils.Constant.TOKEN_NAME;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    LoginUtil loginUtil;
    @Autowired
    TokenRepositoy tokenRepositoy;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 여기서 쿠키 생성 및 세션 관리 로직을 추가할 수 있습니다.
        String accessToken = jwtUtil.generateToken(authentication, true);
        //String refreshToken = jwtUtil.generateToken(authentication, false);
        // 예시: 쿠키 생성
        CookieUtil.makeCookie(response, TOKEN_NAME, accessToken, "/", 3600);

        Member member = (Member)loginUtil.getPrincipal(authentication);
        String defaultTargetUrl ="/";
        switch (member.getRole()){
            case FAMILY, USER ->defaultTargetUrl ="/";
            case ADMIN ->defaultTargetUrl ="/admin";
        }
        setDefaultTargetUrl(loginUtil.isEnabled(authentication) ? defaultTargetUrl : "/login?auth=true");
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}

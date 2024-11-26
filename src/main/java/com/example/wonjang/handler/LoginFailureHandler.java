package com.example.wonjang.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if(exception instanceof BadCredentialsException) { //비밀번호가 일치하지 않을 때 던지는 예외
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) { // 존재하지 않는 아이디일 때 던지는 예외
            errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
        } else if (exception instanceof LockedException) { //인증 거부 - 잠긴 계정
            errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
        } else if (exception instanceof DisabledException) { //인증 거부 - 계정 비활성화
            errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
        } else if (exception instanceof AccountExpiredException) { //인증 거부 - 계정 유효기간 만료
            errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) { //인증 요구가 거부됐을 때 던지는 예외
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        }

        log.error("errorName={}, errorMessage={}", exception.getClass().getName(), errorMessage);
        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8); /* 한글 인코딩 깨진 문제 방지 */
        setDefaultFailureUrl("/login?error");
        request.setAttribute("errorMessage",errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}

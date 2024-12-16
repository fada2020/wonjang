package com.example.wonjang.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public Object nullex(Exception e) {
//        System.err.println(e.getClass());
//        System.err.println(e.getMessage());
//        ModelAndView mv = new ModelAndView("index");
//        return mv;
//    }
    @ExceptionHandler(ServletRequestBindingException.class)
    public String handleSessionAttributeNotFound(ServletRequestBindingException e, Model model) {
        // 로그인 페이지로 리다이렉트
        return "redirect:/login";  // 로그인 페이지로 리다이렉트
    }
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex) {
        // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }
}

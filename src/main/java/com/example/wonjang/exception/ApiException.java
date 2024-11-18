package com.example.wonjang.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestControllerAdvice
public class ApiException {
    @ExceptionHandler(Exception.class)
    public Object nullex(Exception e) {
        System.err.println(e.getClass());
        System.err.println(e.getMessage());
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public Object nullex2(ClassNotFoundException e) {
        System.err.println(e.getClass());
        System.err.println(e.getMessage());
        return "my2222Service";
    }
}

package com.example.wonjang.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // 여기에 로직을 추가하여 500 오류 등 처리

        return "redirect:/"; // customError.html 페이지로 이동
    }

    public String getErrorPath() {
        return "/error";
    }
}

package com.example.wonjang.interceptor;

import com.example.wonjang.model.UserVisit;
import com.example.wonjang.service.UserVisitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class VisitInterceptor implements HandlerInterceptor {
    @Autowired
    private UserVisitService userVisitService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        // 방문 시간을 기록
        if (session.getAttribute("visited") == null) {
            LocalDate localDate = LocalDate.now();
            session.setAttribute("visited", localDate);
            Optional<UserVisit>optionalUserVisit = userVisitService.findByLocalDate(localDate);
            if (optionalUserVisit.isPresent()) {
                optionalUserVisit.ifPresent(userVisit -> {
                    userVisit.plusConnect();
                    userVisitService.save(userVisit);
                });
            } else {
                userVisitService.save(new UserVisit());
            }
        }
        return true;
    }
}
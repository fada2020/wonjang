package com.example.wonjang.interceptor;

import com.example.wonjang.annotation.CurrentUser;
import com.example.wonjang.resolver.CurrentUserArgumentResolver;
import com.example.wonjang.utils.LoginUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.lang.reflect.Parameter;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginUtil loginUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //어노테이션 체크 - Controller에 @Auth 어노테이션이 있는지 확인

        boolean hasAnnotation = checkAnnotation(request, response, handler);
        Authentication authentication = (Authentication) request.getUserPrincipal();
        Object principal = loginUtil.getPrincipal(authentication);
        if(hasAnnotation && principal == null){
            System.out.println(" =/asdfsadfsadfsadfsafdasdfsadf " );
            response.sendRedirect("/login");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkAnnotation(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //js. html 타입인 view 과련 파일들은 통과한다.(view 관련 요청 = ResourceHttpRequestHandler)
        if (handler instanceof ResourceHttpRequestHandler) {
            return false;
        }
        if (handler instanceof HandlerMethod handlerMethod) {
            Parameter[] parameters = handlerMethod.getMethod().getParameters();
            for (Parameter parameter : parameters) {
                CurrentUser currentUser = parameter.getAnnotation(CurrentUser.class);
                if (currentUser != null) {
                    return true;
                }
            }
        }
        //annotation이 없는 경우
        return false;
    }

}
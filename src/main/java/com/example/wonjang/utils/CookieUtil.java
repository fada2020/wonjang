package com.example.wonjang.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void makeCookie(HttpServletResponse response, String name, String value, String path, int time){
        Cookie cookie = new Cookie(name, value); // 실제로는 랜덤하게 생성된 세션 ID를 사용해야 합니다.
        cookie.setPath(path);
        cookie.setMaxAge(time); // 쿠키 유효 시간 설정 (초 단위, 예시로 1시간 유효)
        response.addCookie(cookie);
    }
}

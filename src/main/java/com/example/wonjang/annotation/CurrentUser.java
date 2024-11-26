package com.example.wonjang.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    String value() default "";       // 기본값을 빈 문자열로 설정
    boolean required() default true; // 기본값을 true로 설정
}

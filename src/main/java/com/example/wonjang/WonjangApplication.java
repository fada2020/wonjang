package com.example.wonjang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableJpaRepositories
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화, 권한이 필요한 API에 @Secured 어노테이션 추가하여 권한 설정
public class WonjangApplication {
    // 시작
    public static void main(String[] args) {
        SpringApplication.run(WonjangApplication.class, args);
    }

}

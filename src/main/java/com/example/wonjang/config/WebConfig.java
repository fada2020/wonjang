package com.example.wonjang.config;


import com.example.wonjang.interceptor.AuthInterceptor;
import com.example.wonjang.interceptor.VisitInterceptor;
import com.example.wonjang.repository.MemberRepository;
import com.example.wonjang.resolver.CurrentUserArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file-path.upload-path}")
    private String uploadPath;
    @Autowired
    Environment environment;
    private final Integer CACHE_TIME = 30;
    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final AuthInterceptor authInterceptor;
    private final MemberRepository memberRepository;
    private final VisitInterceptor visitInterceptor;

    public WebConfig(CurrentUserArgumentResolver currentUserArgumentResolver, AuthInterceptor authInterceptor, MemberRepository memberRepository, VisitInterceptor visitInterceptor) {
        this.currentUserArgumentResolver = currentUserArgumentResolver;
        this.authInterceptor = authInterceptor;
        this.memberRepository = memberRepository;
        this.visitInterceptor = visitInterceptor;
    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter(){
        return new ResourceUrlEncodingFilter();
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        VersionResourceResolver versionResourceResolver = new VersionResourceResolver();
        versionResourceResolver.addContentVersionStrategy("/**");
        registry.addResourceHandler("/storage/**")
                .addResourceLocations("file:///"+uploadPath)
                .setCacheControl(CacheControl.maxAge(CACHE_TIME, TimeUnit.DAYS)) // 예시: 30일 동안 캐시 유지
                .resourceChain(true)
                .addResolver(versionResourceResolver);
                //.addResolver(new Utf8DecodeResourceResolver());
            registry
                    .addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/", "classpath:/resources/static/")
                    .setCacheControl(CacheControl.maxAge(CACHE_TIME, TimeUnit.DAYS)) // 예시: 30일 동안 캐시 유지
                    .resourceChain(true)
                    .addResolver(new EncodedResourceResolver())
                    .addResolver(versionResourceResolver);

    }
    
   @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(visitInterceptor);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080"
                        , "http://xn--o55b91v.xn--2i0b10rqve.xn--3e0b707e"
                        , "https://xn--o55b91v.xn--2i0b10rqve.xn--3e0b707e"
                        , "http://hyunjoo.kro.kr"
                        , "https://hyunjoo.kro.kr"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserArgumentResolver);
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true); // 페이지 번호를 1부터 시작
        argumentResolvers.add(resolver);
    }

}

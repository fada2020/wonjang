package com.example.wonjang.config;

import com.example.wonjang.entryPoint.UnauthorizedEntryPoint;
import com.example.wonjang.handler.CustomAccessDeniedHandler;
import com.example.wonjang.handler.CustomAuthenticationSuccessHandler;
import com.example.wonjang.handler.LoginFailureHandler;
import com.example.wonjang.model.Role;
import com.example.wonjang.provider.CustomAuthenticationProvider;
import com.example.wonjang.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.example.wonjang.utils.Constant.TOKEN_NAME;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final MemberService memberService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;

    public SecurityConfig(MemberService memberService, CustomAccessDeniedHandler customAccessDeniedHandler, LoginFailureHandler loginFailureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, UnauthorizedEntryPoint unauthorizedEntryPoint) {
        this.memberService = memberService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.loginFailureHandler = loginFailureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
    }
    @Bean
    CustomAuthenticationProvider customAuthenticationProvider() throws Exception {
        return new CustomAuthenticationProvider(memberService, bCryptPasswordEncoder());
    }
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/storage/**","/favicon.ico", "/css/**", "/js/**");
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults())
                //.csrf(AbstractHttpConfigurer::disable)
                .csrf(t->t.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                //.headers((headerConfig) -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .headers((headerConfig) -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                //.requestMatchers(PathRequest.toH2Console()).permitAll()
                                //.requestMatchers("/", "/member/*").hasRole(Role.USER.name())
                                .requestMatchers("/admin", "/admin/**").hasRole(Role.ADMIN.name())
                                .anyRequest().permitAll()

                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginProcessingUrl("/login-proc")
                                .failureHandler(loginFailureHandler)
                                .defaultSuccessUrl("/", true)
                                .successHandler(customAuthenticationSuccessHandler)
                )
                .logout((logoutConfig) ->
                        logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID", TOKEN_NAME)
                )
                .userDetailsService(memberService)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authenticationProvider(customAuthenticationProvider())		// authenticationProvider 추가해주기
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(customAccessDeniedHandler)
                )
        ;
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH")); // or simply "*"
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

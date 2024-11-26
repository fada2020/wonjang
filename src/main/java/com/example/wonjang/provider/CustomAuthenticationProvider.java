package com.example.wonjang.provider;

import com.example.wonjang.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MemberService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {		// authenticate 메서드
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, loadedUser.getPassword()) && loadedUser.isEnabled()) {
            return new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {		// supports 메서드
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
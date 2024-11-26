package com.example.wonjang.utils;

import com.example.wonjang.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.access.key}")
    private String accessKey;

    @Value("${jwt.refresh.key}")
    private String refreshKey;
    @Value("${jwt.access.time}")
    private Long accessValidTime;

    @Value("${jwt.refresh.time}")
    private Long refreshValidTime;
    @Autowired
    LoginUtil loginUtil;
    public String encodeKey(String key) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        // Base64 인코딩
        return Base64.getEncoder().encodeToString(hash);
    }
    public SecretKey getSigningKey(boolean isAccess)  {
        byte[] keyBytes = Decoders.BASE64.decode(isAccess ? accessKey : refreshKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Date getExpiration(Long time){
        return new Date((new Date()).getTime() + time);
    }
    // JWT 토큰 생성 메서드
    public String generateToken(Authentication authentication, boolean isAccess) {
        // 인증 정보에서 사용자 이름 추출
        Member member = (Member)loginUtil.getPrincipal(authentication);
        String email = member.getEmail();
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(getExpiration(isAccess ? accessValidTime : refreshValidTime))
                .signWith(getSigningKey(isAccess))
                .compact();
    }
    public String generateToken(String email, boolean isAccess) {
        // 인증 정보에서 사용자 이름 추출
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(getExpiration(isAccess ? accessValidTime : refreshValidTime))
                .signWith(getSigningKey(isAccess))
                .compact();
    }
    public Claims extractAllClaims(String token, boolean isAccess) {
        return Jwts.parser()
                .verifyWith(getSigningKey(isAccess))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public  boolean validateToken(String token, boolean isAccess) {
        Claims claims = extractAllClaims(token, isAccess);
        return  !isTokenExpired(claims);
    }

    private  boolean isTokenExpired(Claims claims) {
        try {
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token, boolean isAccess) {
        try {
            return extractAllClaims(token, isAccess)
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }
}

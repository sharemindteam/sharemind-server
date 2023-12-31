package com.example.sharemind.global.jwt;

import com.example.sharemind.auth.repository.RefreshTokenRepository;
import com.example.sharemind.customer.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final CustomUserDetailsService customUserDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final String secret;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;
    private Key key;

    public TokenProvider(CustomUserDetailsService customUserDetailsService,
                         RefreshTokenRepository refreshTokenRepository,
                         @Value("${jwt.secret}") String secret,
                         @Value("${jwt.access-expiration-time}") Long accessExpirationTime,
                         @Value("${jwt.refresh-expiration-time}") Long refreshExpirationTime) {
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.secret = secret;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String email, List<Role> roles) {

        String authorities = roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims()
                .setSubject(email);
        claims.put("authorities", authorities);

        Date expirationTime = getExpirationTime(accessExpirationTime);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + accessToken;
    }

    public String createRefreshToken(String email) {

        Claims claims = Jwts.claims()
                .setSubject(email);

        Date expirationTime = getExpirationTime(refreshExpirationTime);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Duration duration = Duration.between(Instant.now(), expirationTime.toInstant());
        refreshTokenRepository.save(email, refreshToken, duration);

        return "Bearer " + refreshToken;
    }

    public Authentication getAuthentication(String token) {

        String email = parseClaims(token).getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpirationTime(Long expirationTime) {
        return new Date((new Date()).getTime() + expirationTime);
    }
}

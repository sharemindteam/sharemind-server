package com.example.sharemind.global.jwt;

import static com.example.sharemind.global.constants.Constants.TOKEN_PREFIX;

import com.example.sharemind.auth.repository.TokenRepository;
import com.example.sharemind.customer.content.Role;
import io.jsonwebtoken.*;
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

    private final TokenRepository tokenRepository;

    private final String secret;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;
    private Key key;

    public TokenProvider(CustomUserDetailsService customUserDetailsService,
                         TokenRepository tokenRepository,
                         @Value("${jwt.secret}") String secret,
                         @Value("${jwt.access-expiration-time}") Long accessExpirationTime,
                         @Value("${jwt.refresh-expiration-time}") Long refreshExpirationTime) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenRepository = tokenRepository;
        this.secret = secret;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    @Override
    public void afterPropertiesSet() {
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

        return TOKEN_PREFIX + accessToken;
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
        tokenRepository.save(email, refreshToken, duration);

        return refreshToken;
    }

    public Authentication getAuthentication(String token) {

        String email = parseClaims(token).getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Claims claims = parseClaims(accessToken);
            String signOutToken = tokenRepository.findByKey(accessToken);

            return !claims.getExpiration().before(new Date()) && (signOutToken == null);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT입니다.", e);
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 JWT입니다.", e);
        }

        return false;
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = parseClaims(refreshToken);

            String email = claims.getSubject();
            String savedRefreshToken = tokenRepository.findByKey(email);

            return refreshToken.equals(savedRefreshToken);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT입니다.", e);
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 JWT입니다.", e);
        }

        return false;
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Duration getRestExpirationTime(String token) {
        return Duration.between(Instant.now(), parseClaims(token).getExpiration().toInstant());
    }

    public String getTokenWithNoPrefix(String accessToken) {
        return accessToken.replace(TOKEN_PREFIX, "");
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

package com.example.sharemind.global.config;

import com.example.sharemind.global.jwt.JwtAccessDeniedHandler;
import com.example.sharemind.global.jwt.JwtAuthenticationEntryPoint;
import com.example.sharemind.global.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_COUNSELOR = "COUNSELOR";
    private static final String ROLE_ADMIN = "ADMIN";

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( // TODO 여기 더 나은 방법이 있을 것 같은데 일단 동작은 하니까 두고 추후에 리팩토링...ㅠㅠ
                        requests -> requests.requestMatchers("/error", "/swagger-ui/**", "/api-docs/**",
                                        "/api/v1/auth/signUp", "/api/v1/auth/signIn", "/api/v1/auth/reissue",
                                        "/api/v1/auth/find-id", "/api/v1/auth/find-password", "/api/v1/auth/recovery-email/**", "/api/v1/emails/**").permitAll()
                                .requestMatchers("/api/v1/counselors/all/**", "/api/v1/searchWords/results/**", "/api/v1/reviews/all/**").permitAll()
                                .requestMatchers("/index.html", "/favicon.ico", "/chat/**", "/customer.html",
                                        "/counselor.html").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/posts/{postId}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/posts/customers/public/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/comments/customers/{postId}").permitAll()
                                .requestMatchers("/api/v1/admins/**").hasRole(ROLE_ADMIN)
                                .requestMatchers("/api/v1/letters/counselors/**", "/api/v1/reviews/counselors**").hasRole(ROLE_COUNSELOR)
                                .requestMatchers("/api/v1/chats/counselors/**").hasRole(ROLE_COUNSELOR)
                                .requestMatchers("/api/v1/chatMessages/counselors/**").hasRole(ROLE_COUNSELOR)
                                .requestMatchers("/api/v1/consults/counselors").hasRole(ROLE_COUNSELOR)
                                .requestMatchers("/api/v1/payments/counselors/**").hasRole(ROLE_COUNSELOR)
                                .requestMatchers("/api/v1/posts/counselors/**").hasRole(ROLE_COUNSELOR)
                                .requestMatchers("/api/v1/comments/counselors/**").hasRole(ROLE_COUNSELOR)
                                .requestMatchers(("/api/v1/counselors/account")).hasRole(ROLE_COUNSELOR)
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

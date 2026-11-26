package com.springlog.repetitivelearning.config;

import com.springlog.repetitivelearning.security.RestAccessDeniedHandler;
import com.springlog.repetitivelearning.security.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      @Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .httpBasic(Customizer.withDefaults())

        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new RestAuthenticationEntryPoint(resolver))
            .accessDeniedHandler(new RestAccessDeniedHandler(resolver)))

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/error").permitAll()
            // 첨부 조회/다운로드: 로그인 필요 (공개 읽기 규칙보다 먼저 와야 함)
            .requestMatchers(HttpMethod.GET, "/api/activities/*/attachment/download").authenticated()
            .requestMatchers("/api/v1/auth/whoami").authenticated()
            // 공개 읽기
            .requestMatchers(HttpMethod.GET, "/api/activities", "/api/activities/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/health").permitAll()
            // 감사 로그
            .requestMatchers(HttpMethod.GET, "/api/audit-logs").hasAllRoles("ADMIN")
            // 회원가입
            .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
            // 나머지(생성/수정/삭제 등)는 로그인 필요
            .anyRequest().authenticated()
        );
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

package com.sparta.logistics.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session ->
            session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        )
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        .authorizeHttpRequests(authorize ->
            authorize

                /*
                 * 회원가입, 로그인, AccessToken 재발급 허용
                 */
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/auth/signup",
                    "/api/v1/auth/login",
                    "/api/v1/auth/reissue",
                    "/api/v1/auth/logout"
                ).permitAll()

                .requestMatchers(
                    "/api/api-docs",
                    "/api/api-spec",
                    "/api/my-docs",
                    "/api/swagger-ui/**"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.PATCH,
                    "/internal/api/v1/auth/accounts/{userId}/activate",
                    "/internal/api/v1/auth/accounts/{userId}/reject"
                ).permitAll()

                .anyRequest().authenticated()
        );

    return http.build();
  }

  // 비밀번호 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}

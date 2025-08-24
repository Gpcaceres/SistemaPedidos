package com.example.cliente_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .httpBasic(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}


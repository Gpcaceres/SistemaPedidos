package com.example.pedido_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasAuthority("SCOPE_pedidos.read")
        .requestMatchers(HttpMethod.POST, "/api/pedidos/**").hasAuthority("SCOPE_pedidos.write")
        .requestMatchers(HttpMethod.PUT, "/api/pedidos/**").hasAuthority("SCOPE_pedidos.write")
        .requestMatchers(HttpMethod.DELETE, "/api/pedidos/**").hasAuthority("SCOPE_pedidos.write")
        .anyRequest().authenticated())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}

package com.example.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class SecurityConfig {

  /**
   * Convierte el JWT en Authentication agregando:
   *  - SCOPE_* (por defecto con JwtGrantedAuthoritiesConverter)
   *  - ROLE_* leído del claim "roles"
   */
  @Bean
  Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthConverter() {
    // Scopes -> SCOPE_*
    JwtGrantedAuthoritiesConverter scopesConverter = new JwtGrantedAuthoritiesConverter();

    // Roles (claim "roles") -> ROLE_*
    JwtGrantedAuthoritiesConverter rolesConverter = new JwtGrantedAuthoritiesConverter();
    rolesConverter.setAuthoritiesClaimName("roles");
    rolesConverter.setAuthorityPrefix("ROLE_");

    ReactiveJwtAuthenticationConverter reactive = new ReactiveJwtAuthenticationConverter();
    reactive.setJwtGrantedAuthoritiesConverter(jwt -> {
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      Collection<GrantedAuthority> s = scopesConverter.convert(jwt);
      if (s != null) authorities.addAll(s);
      Collection<GrantedAuthority> r = rolesConverter.convert(jwt);
      if (r != null) authorities.addAll(r);
      return Flux.fromIterable(authorities);
    });

    return reactive;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http,
      Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthConverter) {

    http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .cors(Customizer.withDefaults())
      .authorizeExchange(reg -> reg
        .pathMatchers("/actuator/**").permitAll()
        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // Público
        .pathMatchers(HttpMethod.GET, "/api/tracking/**").permitAll()

        // Interno (requiere scope o admin)
        .pathMatchers("/internal/**").hasAnyAuthority("SCOPE_tracking.write", "ROLE_ADMIN")

        // Clientes
        .pathMatchers(HttpMethod.GET, "/api/clientes/**")
          .hasAnyAuthority("SCOPE_clientes.read", "ROLE_ADMIN", "ROLE_CLIENTE")
        .pathMatchers(HttpMethod.POST, "/api/clientes/**")
          .hasAnyAuthority("SCOPE_clientes.write", "ROLE_ADMIN")
        .pathMatchers(HttpMethod.PUT, "/api/clientes/**")
          .hasAnyAuthority("SCOPE_clientes.write", "ROLE_ADMIN")
        .pathMatchers(HttpMethod.DELETE, "/api/clientes/**")
          .hasAnyAuthority("SCOPE_clientes.write", "ROLE_ADMIN")

        // Pedidos
        .pathMatchers(HttpMethod.GET, "/api/pedidos/**")
          .hasAnyAuthority("SCOPE_pedidos.read", "ROLE_ADMIN")
        .pathMatchers(HttpMethod.POST, "/api/pedidos/**")
          .hasAnyAuthority("SCOPE_pedidos.write", "ROLE_ADMIN")
        .pathMatchers(HttpMethod.PUT, "/api/pedidos/**")
          .hasAnyAuthority("SCOPE_pedidos.write", "ROLE_ADMIN")
        .pathMatchers(HttpMethod.DELETE, "/api/pedidos/**")
          .hasAnyAuthority("SCOPE_pedidos.write", "ROLE_ADMIN")

        .anyExchange().authenticated()
      )
      .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(jwtAuthConverter)));

    return http.build();
  }

  @Bean
  public CorsWebFilter corsWebFilter() {
    // Permite configurar orígenes por env. Por defecto, sólo Angular dev.
    String allowedCsv = System.getenv().getOrDefault(
      "CORS_ALLOWED_ORIGINS",
      "http://localhost:4200,http://localhost:3000"
    );

    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOrigins(List.of(allowedCsv.split(",")));

    // Métodos y headers
    cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    cfg.setAllowedHeaders(List.of("*")); // Authorization, Content-Type, etc.
    cfg.setExposedHeaders(List.of("Location","Authorization","WWW-Authenticate"));

    // No usamos cookies/sesión en el browser para el gateway (sólo Bearer)
    cfg.setAllowCredentials(false);

    // Cachea preflight en el browser
    cfg.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return new CorsWebFilter(source);
  }
}

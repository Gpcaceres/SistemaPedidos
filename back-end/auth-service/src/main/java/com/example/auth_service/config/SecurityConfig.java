package com.example.auth_service.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${app.issuer}")
  private String issuer;

  // --- Authorization Server (endpoints OAuth2/OIDC) ---
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
    http.cors(Customizer.withDefaults());
    return http.build();
  }

  // --- Seguridad de la app (login form) ---
  @Bean
  SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/**", "/auth/register", "/auth/login").permitAll()
        .anyRequest().authenticated())
      .formLogin(Customizer.withDefaults());
    return http.build();
  }

  // --- Clientes (técnico, Postman y Frontend PKCE) ---
  @Bean
  RegisteredClientRepository registeredClientRepository(PasswordEncoder encoder) {

    TokenSettings shortLivedTokens = TokenSettings.builder()
      .accessTokenTimeToLive(Duration.ofMinutes(5))   // opcional: ajusta a gusto
      .refreshTokenTimeToLive(Duration.ofDays(1))     // opcional
      .build();

    // microservicio pedidos (client credentials)
    RegisteredClient pedidoService = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientId("pedido-service")
      .clientSecret(encoder.encode("pedido-secret"))
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .scope("tracking.write")
      .scope("clientes.read")
      .scope("pedidos.read")
      .scope("pedidos.write")
      .tokenSettings(shortLivedTokens)
      .build();

    // Postman (client credentials)
    RegisteredClient postmanClient = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientId("postman-client")
      .clientSecret(encoder.encode("postman-secret"))
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .scope("clientes.read")
      .scope("clientes.write")
      .scope("pedidos.read")
      .scope("pedidos.write")
      .scope("tracking.write")
      .tokenSettings(shortLivedTokens)
      .build();

    // SPA (PKCE, sin secret)
    RegisteredClient frontClient = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientId("front-client")
      .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)              // SPA sin secret
      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)        // code + PKCE
      .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)             // refresh
      .redirectUri("http://localhost:4200/callback")
      .postLogoutRedirectUri("http://localhost:4200/")
      .scope("openid").scope("profile")
      .scope("clientes.read")
      .scope("pedidos.read")
      .scope("pedidos.write")                                                   // ← necesario si el SPA crea pedidos
      // .scope("clientes.write")                                               // ← descomenta si el SPA crea/edita clientes
      .clientSettings(ClientSettings.builder()
        .requireProofKey(true)                   // PKCE
        .requireAuthorizationConsent(false)      // no pedir consentimiento en dev
        .build())
      .tokenSettings(shortLivedTokens)
      .build();

    return new InMemoryRegisteredClientRepository(pedidoService, postmanClient, frontClient);
  }

  // Users will be loaded from the database via JpaUserDetailsService

  // --- Password encoder ---
  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  // --- Authentication manager ---
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  // --- Customizer: incluir claim "roles" en access token e id token ---
  @Bean
  OAuth2TokenCustomizer<JwtEncodingContext> addRolesClaim() {
    return context -> {
      var roles = context.getPrincipal().getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)     // ROLE_ADMIN, ROLE_CLIENTE, ...
        .filter(a -> a.startsWith("ROLE_"))
        .map(a -> a.substring(5))                // ADMIN, CLIENTE
        .distinct()
        .toList();

      String tokenType = context.getTokenType() != null
        ? context.getTokenType().getValue()
        : "";

      if ("access_token".equalsIgnoreCase(tokenType) || "id_token".equalsIgnoreCase(tokenType)) {
        context.getClaims().claim("roles", roles);
      }
    };
  }

  // --- JWK (en prod persistir/cargar desde keystore/PEM) ---
  @Bean
  JWKSource<SecurityContext> jwkSource() {
    RSAKey rsaKey = Jwks.generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  // --- Issuer ---
  @Bean
  AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().issuer(issuer).build();
  }

  // --- CORS para el frontend ---
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOrigins(List.of("http://localhost:4200"));
    cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
    cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Cache-Control","X-Requested-With"));
    cfg.setExposedHeaders(List.of("Location"));
    cfg.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }
}

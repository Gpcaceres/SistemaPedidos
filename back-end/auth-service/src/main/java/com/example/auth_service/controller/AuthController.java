package com.example.auth_service.controller;

import com.example.auth_service.dto.AuthResponse;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.user.AppUser;
import com.example.auth_service.user.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final PasswordEncoder passwordEncoder;
  private final AppUserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtEncoder jwtEncoder;

  public AuthController(PasswordEncoder passwordEncoder,
                        AppUserRepository userRepository,
                        AuthenticationManager authenticationManager,
                        JwtEncoder jwtEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtEncoder = jwtEncoder;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
      if (userRepository.existsByUsername(request.username())) {
        return ResponseEntity.badRequest().body(new AuthResponse("User already exists", null, null));
      }
    AppUser user = AppUser.builder()
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .role(request.role().toUpperCase())
        .build();
    userRepository.save(user);
    return ResponseEntity.ok(new AuthResponse("User registered", null, null));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(request.username(), request.password());
    Authentication auth = authenticationManager.authenticate(token);

    Instant now = Instant.now();
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(3600))
        .subject(request.username())
        .claim("roles", auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()))
        .build();
    String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    return ResponseEntity.ok(new AuthResponse("Login successful", accessToken, null));
  }
}

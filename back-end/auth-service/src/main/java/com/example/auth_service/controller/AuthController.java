package com.example.auth_service.controller;

import com.example.auth_service.dto.AuthResponse;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.user.AppUser;
import com.example.auth_service.user.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final PasswordEncoder passwordEncoder;
  private final AppUserRepository userRepository;
  private final AuthenticationManager authenticationManager;

  public AuthController(PasswordEncoder passwordEncoder,
                        AppUserRepository userRepository,
                        AuthenticationManager authenticationManager) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      return ResponseEntity.badRequest().body(new AuthResponse("User already exists"));
    }
    AppUser user = AppUser.builder()
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .role(request.role().toUpperCase())
        .build();
    userRepository.save(user);
    return ResponseEntity.ok(new AuthResponse("User registered"));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(request.username(), request.password());
    authenticationManager.authenticate(token);
    return ResponseEntity.ok(new AuthResponse("Login successful"));
  }
}

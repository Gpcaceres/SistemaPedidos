package com.example.auth_service.controller;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final PasswordEncoder passwordEncoder;
  private final UserDetailsManager userDetailsManager;
  private final AuthenticationManager authenticationManager;

  public AuthController(PasswordEncoder passwordEncoder,
                        UserDetailsManager userDetailsManager,
                        AuthenticationManager authenticationManager) {
    this.passwordEncoder = passwordEncoder;
    this.userDetailsManager = userDetailsManager;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    if (userDetailsManager.userExists(request.username())) {
      return ResponseEntity.badRequest().body("User already exists");
    }
    UserDetails user = User.withUsername(request.username())
        .password(passwordEncoder.encode(request.password()))
        .roles(request.role().toUpperCase())
        .build();
    userDetailsManager.createUser(user);
    return ResponseEntity.ok("User registered");
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(request.username(), request.password());
    authenticationManager.authenticate(token);
    return ResponseEntity.ok("Login successful");
  }
}

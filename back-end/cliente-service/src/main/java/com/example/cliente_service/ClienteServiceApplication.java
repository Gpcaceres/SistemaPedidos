package com.example.cliente_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// importa esta clase:
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class ClienteServiceApplication {
  public static void main(String[] args) { SpringApplication.run(ClienteServiceApplication.class, args); }
}

package com.example.pedido_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class PedidoServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(PedidoServiceApplication.class, args);
  }
}

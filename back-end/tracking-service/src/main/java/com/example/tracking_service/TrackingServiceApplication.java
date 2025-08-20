package com.example.tracking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class TrackingServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(TrackingServiceApplication.class, args);
  }
}

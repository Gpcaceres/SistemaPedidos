package com.example.auth_service.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 120)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 30)
  private String role;
}

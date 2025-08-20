package com.example.cliente_service.clientes;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

  @Id
  @GeneratedValue       // Genera el UUID automáticamente
  @UuidGenerator
  private UUID id;

  @NotBlank
  @Size(max = 120)
  @Column(nullable = false, length = 120)
  private String nombre;

  @NotBlank
  @Email
  @Size(max = 160)
  @Column(unique = true, nullable = false, length = 160)
  private String email;

  @Size(max = 40)
  @Column(length = 40)
  private String telefono;

  @Column(name = "creado_en", nullable = false, columnDefinition = "timestamptz")
  @Builder.Default
  private OffsetDateTime creadoEn = OffsetDateTime.now();

  @PrePersist
  void prePersist() {
    if (creadoEn == null) {
      creadoEn = OffsetDateTime.now();
    }
  }
}

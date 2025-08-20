package com.example.pedido_service.pedidos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "cliente_id", nullable = false, columnDefinition = "BINARY(16)")
  @NotNull
  private UUID clienteId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private PedidoEstado estado = PedidoEstado.CREADO;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = true)
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal total;

  @Column(name = "creado_en", nullable = false)
  @Builder.Default
  private OffsetDateTime creadoEn = OffsetDateTime.now();

  @Column(name = "actualizado_en", nullable = false)
  @Builder.Default
  private OffsetDateTime actualizadoEn = OffsetDateTime.now();

  @PrePersist
  void prePersist() {
    if (creadoEn == null) creadoEn = OffsetDateTime.now();
    if (actualizadoEn == null) actualizadoEn = OffsetDateTime.now();
  }

  @PreUpdate
  void preUpdate() {
    actualizadoEn = OffsetDateTime.now();
  }
}

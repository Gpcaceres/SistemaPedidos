package com.example.pedido_service.pedidos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoCreateReq {
  @NotNull
  private UUID clienteId;

  @NotNull @DecimalMin(value = "0.0", inclusive = true)
  private BigDecimal total;
}

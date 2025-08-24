package com.example.tracking_service.tracking;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrackingEntry {
  @NotNull
  private UUID pedidoId;
  @NotNull
  private UUID clienteId;
  @NotNull
  private PedidoEstado estado;
  @NotNull
  private BigDecimal total;
  @NotNull
  private OffsetDateTime actualizadoEn;
}

package com.example.pedido_service.pedidos.dto;

import com.example.pedido_service.pedidos.PedidoEstado;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoUpdateReq {
  @Nullable @DecimalMin(value = "0.0", inclusive = true)
  private BigDecimal total;

  @Nullable
  private PedidoEstado estado;
}

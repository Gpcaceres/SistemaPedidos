package com.example.cliente_service.clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {}

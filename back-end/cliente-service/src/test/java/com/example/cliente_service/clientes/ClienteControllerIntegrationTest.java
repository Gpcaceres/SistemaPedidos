package com.example.cliente_service.clientes;

import com.example.cliente_service.clientes.dto.ClienteReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerIntegrationTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper mapper;
  @Autowired ClienteRepository repo;

  @BeforeEach
  void setUp() {
    repo.deleteAll();
  }

  @Test
  void crearConEmailDuplicadoRetornaConflict() throws Exception {
    repo.save(Cliente.builder().nombre("A").email("a@a.com").telefono("123").clave("x").build());
    var req = new ClienteReq("B", "a@a.com", "321", "y");
    mvc
        .perform(
            post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
        .andExpect(status().isConflict());
  }

  @Test
  void actualizarConEmailDeOtroRetornaConflict() throws Exception {
    repo.save(Cliente.builder().nombre("A").email("a@a.com").telefono("111").clave("x").build());
    var otro =
        repo.save(Cliente.builder().nombre("B").email("b@b.com").telefono("222").clave("y").build());
    var req = new ClienteReq("B2", "a@a.com", "222", "y");
    mvc
        .perform(
            put("/api/clientes/" + otro.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
        .andExpect(status().isConflict());
  }
}


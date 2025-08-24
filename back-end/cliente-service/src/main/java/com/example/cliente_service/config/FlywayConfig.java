package com.example.cliente_service.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
  @Bean
  public FlywayMigrationStrategy repairStrategy() {
    return flyway -> {
      flyway.repair();
      flyway.migrate();
    };
  }
}

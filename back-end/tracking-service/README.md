# tracking-service

Microservicio de tracking (Spring Boot 3.5 + Java 21 + Redis).

- API lectura: `GET /api/tracking/{pedidoId}`
- API interna de actualización: `POST /internal/tracking/update`
- Puertos: API 8083, Redis 6380 (host)

## Levantar
```bash
docker compose up --build
```

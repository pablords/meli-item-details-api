# meli-item-details-api

API de detalhes de item (estilo MELI) para página de produto: detalhes e recomendações. Projeto em Java 17, Spring Boot 3.4, arquitetura hexagonal baseada em serviços, armazenamento em JSON, observabilidade e testes.

## Stack
- Java 17
- Spring Boot 3.4
- Armazenamento: arquivos JSON (`data/products.json`, `data/sellers.json`)
- Observabilidade: Actuator
- Documentação: OpenAPI/Swagger
- Cache: Caffeine (TTL configurável)

## Arquitetura
Hexagonal (Ports & Adapters):
- **domain**: entidades, value objects, portas (`ProductRepository`)
- **service**: serviços responsáveis pela lógica de negócio (ex: `ProductService`)
- **infrastructure/adapters**: implementação das portas (ex: `FileProductRepository`)
- **app**: configuração Spring Boot, controllers, docs, métricas, health checks

## Endpoints principais
- `GET /api/v1/products/{id}` — Detalhe do produto (inclui seller)
- `GET /api/v1/products/{id}/recommendations?limit=6` — Recomendações por categoria/marca
- `GET /actuator/health` — Health check
- `GET /docs` — Swagger UI

## Dados e índices
- Persistência via JSON
- Índices em memória: categoria, marca

## Recomendações
- Heurística: mesma categoria/marca, exclui o próprio item
- Limite configurável

## Cache
- Detalhe por ID cacheado via Caffeine (TTL)
- Índices evitam varredura O(n)

## Observabilidade
- Actuator: health, métricas
- Logs estruturados

## Testes
- Integração: MockMvc cobrindo rotas principais
- Extensões sugeridas: contract tests, unidades para índice/recomendações

## ADRs
Decisões arquiteturais documentadas em `docs/ADR-00X.md`.

## Estrutura de pastas
```
src/
  main/java/com/pablords/meli/itemdetail/
    adapters/
    config/
    domain/
    service/
    Application.java
  resources/
    application.yml
    data/products.json
    data/sellers.json
  test/java/com/pablords/meli/itemdetail/
    component/
    integration/
    unit/
    utils/
  test/resources/
    data/expected-responses/
    features/
```

## Como rodar local
1. Requisitos: Java 17, Maven
2. Build: `./mvnw clean package`
3. Rodar: `./mvnw spring-boot:run` ou via Docker (`docker-compose up`)
4. Acessar Swagger: [http://localhost:8080/docs](http://localhost:8080/docs)

## Sugestões de evolução
- Feature flags para heurísticas de recomendação
- ETag/Last-Modified nos detalhes para HTTP cache
- Rate limit adaptativo por IP/rota
- Data versioning e invalidar cache por versão
- Contract tests para garantir compatibilidade
- CI/CD: lint, test, build, docker, scan



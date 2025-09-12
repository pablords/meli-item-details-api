# meli-item-details-api

# visão executiva (o que você vai apresentar)

* **objetivo**: API de detalhes do item (estilo MELI) fornecendo dados para a página de produto: detalhes, recomendações e busca.
* **stack**: Node 20 + TS 5, Express, Zod (validação), Pino (logs), Prometheus metrics, LRU cache, OpenAPI (Swagger UI), Jest + Supertest.
* **arquitetura**: **Hexagonal (Ports & Adapters)** para permitir trocar JSON por DB real depois sem reescrever domínio/casos de uso.


# endpoints e contratos

* `GET /api/v1/products/:id` → detalhe do produto (inclui **seller**).
* `GET /api/v1/products/:id/recommendations?limit=6` → recomendações por categoria/marca.
* `GET /api/v1/search?q=&limit=&offset=` → busca com paginação.
* `GET /healthz`, `GET /readyz` → probes.
* `GET /metrics` → métricas Prometheus.
* `GET /docs` → Swagger UI a partir do `openapi.yaml`.

> O **OpenAPI** já está no repo (`openapi.yaml`). Isso viabiliza validação de contrato com o front e “API-first”.

# principais casos (o que justificar na entrevista)

1. **Domínio limpo + Casos de uso**


2. **Busca performática sem DB**

   * Índice em memória por **tokens** (título), **categoria** e **marca** (maps de `token -> set(productId)`).
   * Filtro por termos, paginação simples e contagem total.

3. **Recomendações mínimas, porém coerentes**

   * Heurística: mesmo `category` e mesma `brand`, excluindo o próprio item; limite configurável.

4. **Cache LRU com TTL**

   * Cache de detalhe por id, TTL configurável (`CACHE_TTL_MS`), invalidação automática por expiração.

5. **Observabilidade e SRE-friendly**

   * Métrica `http_request_duration_ms{method,route,status}` pronta para painéis.

6. **Segurança e resiliência**

   * `helmet`, `rateLimit` global (eg. 300 req/min), `CORS` restrito, tamanho de payload limitado, validações (zod).
   * (Ponto de evolução) Circuit-breaker para adaptadores externos — deixei *placeholder* arquitetural.

7. **Qualidade e testes**

8. **Operação e DX**

   * `run.md` com fluxo de execução local, build, testes, Docker.
   * `prompts.md` — transparência do uso de GenAI na produtividade (pedido do desafio).

# estrutura de pastas (hexagonal)

```

```

# como rodar local



# sugestões de evolução (se quiser brilhar na conversa)

* **Feature flags** para alternar heurísticas de recomendação.
* **ETag/Last-Modified** nos detalhes para economizar banda (HTTP cache do front).
* **Search index**: normalização PT-BR (acentos), ranqueamento TF-IDF leve.
* **Rate limit adaptativo** por IP/rota.
* **Data versioning** em `data/` e invalidar cache por versão.
* **Contract tests** com `jest-openapi` para garantir compatibilidade de resposta.
* **CI** (GitHub Actions): lint + test + build + docker + scan.



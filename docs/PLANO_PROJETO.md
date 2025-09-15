# Plano do Projeto - meli-item-details-api

## 1. Visão Geral
Aplicação de detalhe de item (estilo Mercado Livre) fornecendo informações de produto, vendedor, recomendações e avaliações. Foco em leitura rápida, baixa latência e simplicidade operacional (arquivos JSON + cache em memória). Desenvolvido em Java 17 e Spring Boot 3 com arquitetura hexagonal.

## 2. Objetivos
- Expor endpoint de detalhe de produto com vendedor associado.
- Fornecer recomendações simples por categoria/marca.
- Permitir consulta paginada e ordenada de reviews.
- Oferecer resumo de ratings do produto.
- Manter arquitetura clara e testável (unit, component, integration).
- Demonstrar decisões arquiteturais através de ADRs.

### Objetivos Secundários / Nice-to-haves
- Cache configurável (TTL) de produtos (implementado).
- Observabilidade futura (métrica/tracing) — adiado (registro em ADR).

## 3. Escopo
### Incluído
- Leitura de datasets estáticos (products, sellers, reviews) via arquivos JSON embarcados.
- Recomendações heurísticas simples (categoria + marca).
- Índices em memória para lookup rápido.
- Ordenações de reviews: recent, helpful, rating_asc, rating_desc.
- Resumo estatístico de rating (média e histograma 1..5).
- Exposição HTTP REST + OpenAPI.
- Estratégia de testes segmentada.

### Excluído (Fora de Escopo Atual)
- Escrita/mutação de entidades (produto, review, seller).
- Autenticação/Autorização.
- Persistência em banco relacional ou NoSQL.

## 4. Stakeholders / Usuários
- Time técnico avaliador do desafio.

## 5. Arquitetura
### Estilo
Hexagonal (Ports & Adapters). Camadas principais:
- domain: entidades, value objects, portas.
- application/service: orquestra regras e políticas simples.
- adapters inbound: controllers HTTP + DTOs.
- adapters outbound: acesso a datasets e pré-processamento (índices, sorting, cache).

### Principais Componentes
- `ProductService`: lógica de negócio e coordenação.
- `FileProductRepositoryAdapter`: carrega produtos e vendedores, cria índices e cache.
- `FileReviewRepositoryAdapter`: carrega reviews, pré-ordena e produz estatísticas.
- DTOs de resposta: formatação final para API.
- Cache: Caffeine com TTL configurável (`cache.ttl-ms`).

### Decisões Arquiteturais (ADRs)
Principais decisões documentadas (resumo):
- Armazenamento em JSON para simplicidade inicial.
- Índices in-memory para evitar custo de varredura.
- Uso de cache Caffeine para reduzir latência do detalhe.
- Adoção de imutabilidade + builders em entidades de domínio (Product, Review).
- Adiamento de observabilidade completa.
- Manutenção do nome interno `Product` apesar de semântica de “Item” na API.

## 6. Modelo de Dados (simplificado)
- Product: id, title, brand, category, price(Money), pictures, attributes, quantity, sellerId.
- Seller: id, nickname, rating.
- Review: id, productId, rating, title, body, author, verifiedPurchase, helpfulVotes, createdAt, locale.
- RatingSummary: count total, average, histograma de 1..5.

## 7. Fluxo Principal (Detalhe de Produto)
1. Controller recebe GET /products/{id}.
2. Service busca Product (cache first) + Seller.
3. Monta DTO de resposta.


## 8. Performance & Escalabilidade
- Cache reduz leituras e serialização para requisições repetidas.
- Pré-sorting de reviews evita sort on-demand (trade-off: memória vs CPU por requisição).

## 9. Segurança
- Validações de entrada com Bean Validation (limites de tamanho e range).
- Sanitização implícita: resposta é derivada de dataset controlado.

## 10. Testes
Níveis:
- Unit: Regras de domínio e serviços isolados.
- Component: Controllers + stack parcial.
- Integration: Caminho end-to-end com datasets reais (arquivos).
Critérios de aceite: endpoints principais retornam 200/404 coerentes, ordenações de reviews corretas, recomendações não incluem o próprio produto.

## 11. Observabilidade (Planejado / Adiado)
- Health endpoint via Actuator (implementado).
- Logging estruturado: já existem logs de debug por endpoint.


## 12. Riscos e Mitigações
| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| Cache inconsistente (dados desatualizados) | Baixo (dataset estático) | TTL baixo configurável; invalidar em reload futuro |


## 13. Cronograma (Estimado / Referência)
| Fase | Atividades | Status |
|------|------------|--------|
| F1 | Setup, leitura JSON, endpoints básicos | Concluído |
| F2 | Índices, recomendações, reviews | Concluído |
| F3 | Testes multi-nível e ADRs | Concluído |
| F4 | Observabilidade ampliada | Adiado |



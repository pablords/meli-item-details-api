## Rubrica (o que pesa de verdade)

1. **Aderência ao enunciado (10–15%)**

   * Sem banco real (nada de H2).
   * Leitura de JSON/CSV, app sobe fácil (README claro, `mvn package && java -jar …`).

2. **Design & Arquitetura (20–25%)**

   * **Hexagonal/Ports & Adapters** de verdade (controllers → use cases → ports; infra pluggable).
   * Domínio bem modelado (Entity/VO/DTO bem separados).
   * Baixo acoplamento, alta coesão, nomes e pacotes idiomáticos.

3. **Qualidade de código (15–20%)**

   * Simplicidade, legibilidade, null-safety, imutabilidade onde faz sentido.
   * `equals/hashCode` corretos, coleções imutáveis em DTO/VO, erros tipados.

4. **Contratos & DX (10–15%)**

   * **OpenAPI/Swagger** bem descrito (tipos, exemplos, status codes).
   * Erros padronizados (404, 400, 422), paginação consistente (`limit/offset/total`).

5. **Performance & Estruturas (10–15%)**

   * Índices em memória (tokenização de título, maps por categoria/marca).
   * **Cache** (TTL, invalidação simples), I/O eficiente.
   * Respostas rápidas em hot paths (detalhe do item, recomendações).

6. **Observabilidade & Operação (10–15%)**

   * **Actuator** (`/health`, `/prometheus`) e logs estruturados.
   * Métricas por rota (latência, contagem, status) e readiness.

7. **Testes (10–15%)**

   * Unit + integração com MockMvc (principais fluxos e erros).
   * Cobertura mínima razoável do core (não precisa 100%, mas mostre intenção).

---

## O que eles costumam “cutucar”

* **Escalabilidade do “search”**: como evita full-scan sempre? (índice simples + paginação).
* **Recomendações**: regra mínima mas coerente (categoria/marca), limites e bordas.
* **Consistência de resposta**: contratos estáveis, ETag/Cache-Control (bônus).
* **Erros e casos limite**: ID inexistente, `limit/offset` inválidos, arquivo ausente/corrompido.
* **Thread-safety**: leitura concorrente, cache, e se houver “reload” do dataset.
* **Evolutividade**: como trocar o adapter de arquivo por outro (CSV, HTTP, DB no futuro).

---

## Check rápido antes de entregar

* `README` com **um comando** para rodar e endpoints principais.
* `/docs` abre e espelha fielmente as respostas.
* `GET /api/v1/products/{id}` 200/404; `GET /search` pagina; `GET /…/recommendations` respeita `limit`.
* Actuator **exposto**; métricas aparecem.
* Testes passando (`mvn test`).
* Sem dependências “fora do escopo” (H2, JPA, etc.).

---

## Armadilhas comuns (evite)

* Implementar **escrita** sem necessidade → foge do escopo e abre flancos (concorrência, atomicidade).
* Misturar **infra no controller** (quebra o hexagonal).
* **OpenAPI incompleto** (sem schemas/exemplos) ou divergente do retorno real.
* Falta de **tratamento de erro** e mensagens opacas.
* **Busca O(n)** em toda chamada sem índice (degrada rápido).

---

Se quiser, adiciono no seu projeto uma **página “/docs/evaluation”** resumindo essas escolhas (trade-offs, ADRs curtos) para você **apresentar como Tech Lead** na hora da banca.

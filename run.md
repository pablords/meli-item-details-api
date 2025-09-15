## Runbook / Como rodar a aplicação

Este documento centraliza instruções para build, execução local, via Docker (com e sem hot reload), além de comandos úteis de troubleshooting.

### Requisitos
- Java 17 (Temurin ou similar)
- Maven (wrapper incluído: `./mvnw`)
- Docker & Docker Compose (para execução containerizada)

### Build
Build completo (gera JAR em `target/`):
```
./mvnw clean package
```

Sem rodar testes:
```
./mvnw clean package -DskipTests
```

### Execução local (Maven)
Rodar aplicação Spring Boot diretamente:
```
./mvnw spring-boot:run
```

Perfil ativo padrão: `dev` (definido em `application.yml` se configurado). Para forçar um perfil:
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Endpoints úteis
- Swagger UI: http://localhost:8080/api/v1/swagger-ui/index.html
- Health: http://localhost:8080/api/v1/actuator/health

### Execução com Docker (produção simplificada)
Sobe container com a aplicação empacotada:
```
docker-compose up meli-item-details-api
```

Rebuild da imagem caso código mude:
```
docker-compose build meli-item-details-api
```

### Execução com Docker (hot reload / dev)
Usa `Dockerfile.dev` para montar volume do código fonte e permitir reinícios rápidos:
```
docker-compose up meli-item-details-api-dev
```

Antes, garantir permissão de execução do script auxiliar:
```
chmod +x .docker/docker_run_hot_reload.sh
```

### Perfis de Teste
Perfis dedicados para escopos distintos (executam somente seus testes):
```
./mvnw test -Punit-test
./mvnw test -Pcomponent-test
./mvnw test -Pintegration-test
```

### Limpeza de artefatos
```
./mvnw clean
```

### Variáveis / Configuração
Configurações principais em `src/main/resources/application*.yml`.

Ordem de resolução Spring (simplificado):
1. Args de linha de comando
2. Variáveis de ambiente (`SPRING_...`)
3. Arquivos `application-{profile}.yml`

### Logs
Por padrão (dev) nível INFO. Para ajustar dinamicamente:
```
./mvnw spring-boot:run -Dspring-boot.run.arguments="--logging.level.root=DEBUG"
```

### Troubleshooting
| Sintoma | Possível causa | Ação |
|---------|----------------|------|
| Porta 8080 já em uso | Outro serviço em execução | Encerrar processo (`lsof -i :8080`) |
| Swagger não abre | App não subiu / erro inicialização | Ver logs no terminal |
| Mudanças não refletem (dev docker) | Volume não montado ou sem rebuild | Verificar `docker-compose.yml` e permissões |
| Erros de charset em leitura JSON | Arquivo salvo com encoding incorreto | Garantir UTF-8 |

### Curl básicos
Produto:
```
curl -s http://localhost:8080/api/v1/products/MLB-001 | jq
```

Recomendações:
```
curl -s 'http://localhost:8080/api/v1/products/MLB-001/recommendations?limit=6' | jq
```

Reviews (ordenado por recentes):
```
curl -s 'http://localhost:8080/api/v1/products/MLB-001/reviews?sort=recent&limit=5' | jq
```

### Estrutura de dados
Arquivos fonte de dados em `src/main/resources/data/` carregados em memória na inicialização.


### Observabilidade (futuro)
ADR específica (`ADR-007`) registra decisão de adiar instrumentação manual. Quando adicionar metrics/traces, revisar este runbook.

### Fluxo CI/CD (resumo)
Build → Testes (unit/component/integration) → Artefato → Docker Image → Deploy (placeholder). Detalhes no `README.md`.



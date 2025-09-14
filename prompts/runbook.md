# Prompt: Criação de Runbook (rum.md / RUNBOOK.md)

Objetivo: Gerar um runbook operacional separado do README cobrindo build, execução, testes, troubleshooting e comandos utilitários.

Instruções ao modelo:
1. Verificar stack (Java, Spring Boot, Maven, Docker). 
2. Organizar seções em ordem de uso: Requisitos → Build → Execução Local → Execução Docker (produção e dev/hot reload) → Testes por perfil → Variáveis de Configuração → Logs → Troubleshooting → Curl Examples → Manutenção (atualizar dependências) → Observabilidade futura → CI/CD resumo.
3. Cada bloco com comandos em blocos de código shell (` ``` `) simples, um comando por linha.
4. Explicar somente o essencial (evitar repetir arquitetura / decisões já no README).
5. Indicar onde editar configurações (`application.yml`, perfis) e como sobrescrever via args/ENV.
6. Adicionar tabela de troubleshooting (Sintoma / Causa / Ação).
7. Incluir exemplos de curl para endpoints principais.
8. Finalizar com seção de evolução (ex: adicionar métricas, autenticação, versionamento de API).
9. Garantir consistência com endpoints reais.
10. Usar pt-BR claro e direto.

Esqueleto sugerido:
```
## Runbook / Como rodar a aplicação
### Requisitos
...
### Build
...
### Execução Local
...
### Docker (produção)
...
### Docker (dev hot reload)
...
### Testes
...
### Variáveis / Configuração
...
### Logs
...
### Troubleshooting
| Sintoma | Causa | Ação |
### Curls
...
### Manutenção
...
### Observabilidade (futuro)
...
### Evolução
...
```

Checklist:
- [ ] Nenhum comando faltando backticks
- [ ] Endpoints corretos
- [ ] Tabela de troubleshooting alinhada
- [ ] Sem duplicar conteúdo extenso do README

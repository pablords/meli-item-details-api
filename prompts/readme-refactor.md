# Prompt: Refactor de README

Objetivo: Modernizar e alinhar o README com o estado atual do projeto, destacando arquitetura, execução, testes, decisões e pipeline.

Instruções ao modelo:
1. Analise o código fonte (estrutura de pastas) e as ADRs existentes.
2. Identifique seções que devem constar: Visão Geral, Stack, Arquitetura (breve + link ADR-001), Endpoints principais (tabela ou bullets), Dados & Armazenamento, Estratégia de Testes, Execução (ou apontar para runbook), Decisões Arquiteturais (link), Pipeline CI/CD, Próximos Passos / Evolução.
3. Remover referências a tecnologias não utilizadas (ex.: Prometheus, busca textual) se ainda estiverem presentes.
4. Usar tom objetivo e escaneável, preferindo listas curtas.
5. Incluir badges opcionais se disponíveis (build status, cobertura). Se ausentes, comentar onde encaixar.
6. Limite aproximado de cada seção crítica: 8–12 linhas.
7. Evitar duplicação: se runbook existir (`rum.md` ou `RUNBOOK.md`), apenas referenciar.
8. Garantir que todos endpoints listados existam nos controllers.
9. Referenciar ADRs com caminho relativo `docs/ADR-00X.md`.
10. Inserir sessão "Uso de GenAI" listando áreas assistidas (ex.: geração ADRs, refactor README, criação runbook, melhorias repositório).

Formato sugerido:
```
# <nome-do-projeto>
Breve frase de valor.

## Stack
...

## Arquitetura
Resumo + link ADR-001.

## Endpoints
| Método | Caminho | Descrição |

## Dados & Armazenamento
...

## Testes
...

## Execução
Ver runbook.

## CI/CD
Resumo pipeline.

## Decisões Arquiteturais
Lista/links.

## Uso de GenAI
Bullets.

## Próximos Passos
Bullets.
```

Checklist antes de entregar:
- [ ] Nenhum endpoint inexistente
- [ ] Todas as seções presentes
- [ ] Sem tecnologias não utilizadas
- [ ] Links relativos funcionais
- [ ] Tom consistente (pt-BR)

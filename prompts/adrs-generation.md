# Prompt: Geração Inicial de ADRs

Objetivo: Extrair decisões arquiteturais implícitas/explicitadas em um documento de avaliação ou descrição do projeto e gerar arquivos `docs/ADR-00X-<slug>.md` consistentes.

Instruções ao modelo:
1. Leia o documento fonte README.md e liste candidatos a decisões.
2. Classifique-as: Arquitetura, Persistência, Observabilidade, Resiliência, Testes, Dados, Performance, Organização de Código.
3. Priorize as que impactam design inicial (até 10 primeiras).
4. Para cada decisão, gerar arquivo com seções: Contexto, Decisão, Justificativa, Consequências, Alternativas, Riscos, Próximos Passos.
5. Nome do arquivo: `ADR-XYZ-<slug>.md` onde XYZ inicia em 001 ou continua a sequência existente.
6. Manter linguagem concisa (máx ~250 palavras por ADR) e usar linguagem consistente (português brasileiro).
7. Evitar sobreposição: se duas decisões forem variações, combinar.
8. Inserir links cruzados entre ADRs relacionados (`Ver também: ADR-00N`).

Formato de saída (exemplo):
```
# ADR-00X — Nome Curto da Decisão

## Contexto
Breve cenário que levou à decisão.

## Decisão
Frase direta descrevendo o que foi decidido.

## Justificativa
Bullet points explicando motivos principais.

## Consequências
- Positivas
- Negativas / trade-offs

## Alternativas Consideradas
1. Alternativa A — por que rejeitada
2. Alternativa B — por que rejeitada

## Riscos
- Risco 1 e mitigação

## Próximos Passos / Evolução
O que dispara revisão desta ADR.

Ver também: ADR-00Y, ADR-00Z
```

Exemplo de decisão extraída:
Entrada (trecho): "Projeto simples para fornecer detalhes do item usando JSON em disco sem DB".
Saída (resumida): "Decisão: Persistir dados em arquivos JSON locais para eliminar overhead operacional de banco no escopo do desafio".

Checklist de qualidade antes de finalizar:
- [ ] Numeração sequencial correta
- [ ] Slug minúsculo, hifenizado
- [ ] Links cruzados válidos
- [ ] Nenhuma ADR > 250 palavras
- [ ] Sem ambiguidade (evitar termos vagos)

Observação: Se já existirem ADRs, não reescrever — apenas gerar novas ausentes ou marcar necessidade de atualização.

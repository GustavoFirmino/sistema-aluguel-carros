# Regras de Negócio — Sistema de Aluguel de Carros

## RN01 — Unicidade de CPF de Cliente
O CPF de um cliente deve ser único no sistema. Tentativas de cadastro ou atualização com um CPF já existente resultam em erro 422 (Unprocessable Entity).

**Aplicação:** `CriarClienteUseCase`, `AtualizarClienteUseCase`

---

## RN02 — Unicidade de Placa de Automóvel
A placa de um automóvel deve ser única no sistema. Tentativas de cadastro ou atualização com uma placa já cadastrada resultam em erro 422.

**Aplicação:** `CriarAutomovelUseCase`, `AtualizarAutomovelUseCase`

---

## RN03 — Unicidade de Matrícula de Automóvel
A matrícula de um automóvel deve ser única no sistema. Tentativas de cadastro ou atualização com uma matrícula já cadastrada resultam em erro 422.

**Aplicação:** `CriarAutomovelUseCase`, `AtualizarAutomovelUseCase`

---

## RN04 — Disponibilidade do Automóvel para Aluguel
Um pedido de aluguel somente pode ser criado se o automóvel selecionado estiver marcado como disponível (`disponivel = true`). Caso contrário, o sistema retorna erro 422.

**Aplicação:** `CriarPedidoUseCase`

---

## RN05 — Indisponibilidade do Automóvel Após Pedido
Ao criar um pedido de aluguel com sucesso, o automóvel selecionado é automaticamente marcado como indisponível (`disponivel = false`). Isso impede que dois pedidos sejam criados para o mesmo veículo simultaneamente.

**Aplicação:** `CriarPedidoUseCase`

---

## RN06 — Liberação do Automóvel Após Cancelamento ou Rejeição
Quando um pedido é cancelado pelo cliente ou rejeitado por um agente, o automóvel associado volta a ser marcado como disponível (`disponivel = true`).

**Aplicação:** `CancelarPedidoUseCase`, `AtualizarStatusPedidoUseCase`

---

## RN07 — Período de Aluguel Válido
A data de início do aluguel deve ser anterior à data de fim. O sistema rejeita pedidos onde `dataFim <= dataInicio` com erro 422.

**Aplicação:** `CriarPedidoUseCase`

---

## RN08 — Status Inicial do Pedido
Todo pedido criado começa com o status `PENDENTE`. O cliente não pode definir o status inicial no momento da criação.

**Aplicação:** `CriarPedidoUseCase`

---

## RN09 — Fluxo de Transição de Status do Pedido
As transições de status de um pedido seguem o fluxo abaixo. Qualquer tentativa de transição fora deste fluxo resulta em erro 422:

```
PENDENTE    → EM_ANALISE   (agente inicia análise)
PENDENTE    → CANCELADO    (cliente cancela)
EM_ANALISE  → APROVADO     (agente aprova)
EM_ANALISE  → REJEITADO    (agente rejeita)
APROVADO    → CONCLUIDO    (agente conclui o contrato)
```

Pedidos nos estados `REJEITADO`, `CANCELADO` e `CONCLUIDO` são terminais — não aceitam mais transições.

**Aplicação:** `AtualizarStatusPedidoUseCase`

---

## RN10 — Cancelamento Restrito ao Status PENDENTE
O cliente somente pode cancelar um pedido que esteja com status `PENDENTE`. Pedidos em análise, aprovados, rejeitados ou concluídos não podem ser cancelados pelo endpoint de cliente.

**Aplicação:** `CancelarPedidoUseCase`

---

## RN11 — Dados Obrigatórios do Cliente
Para o cadastro de um cliente, os seguintes campos são obrigatórios e não podem ser vazios: `nome`, `cpf`, `rg`, `endereco` e `profissao`.

**Aplicação:** `ClienteRequestDTO` (Jakarta Validation `@NotBlank`)

---

## RN12 — Dados Obrigatórios do Automóvel
Para o cadastro de um automóvel, os seguintes campos são obrigatórios: `matricula`, `ano`, `marca`, `modelo` e `placa`. O campo `ano` deve ser maior ou igual a 1900.

**Aplicação:** `AutomovelRequestDTO` (Jakarta Validation)

---

## RN13 — Dados Obrigatórios do Pedido
Para a criação de um pedido, os seguintes campos são obrigatórios: `clienteId`, `automovelId`, `dataInicio` e `dataFim`. O campo `observacao` é opcional.

**Aplicação:** `PedidoRequestDTO` (Jakarta Validation)

---

## RN14 — Respostas de Erro Padronizadas
Todos os erros retornados pela API seguem o formato padronizado:

```json
{
  "status": 422,
  "error": "Regra de negócio violada",
  "message": "Descrição detalhada do erro",
  "path": "/pedidos",
  "timestamp": "2025-04-10T10:30:00"
}
```

| Situação                         | Código HTTP |
|----------------------------------|-------------|
| Recurso não encontrado           | 404         |
| Violação de regra de negócio     | 422         |
| Dados inválidos (validação)      | 400         |
| Erro interno do servidor         | 500         |

**Aplicação:** `GlobalExceptionHandler`

# Histórias de Usuário — Sistema de Aluguel de Carros

## Épico 1: Gestão de Clientes

### US01 — Cadastrar cliente
**Como** um novo usuário  
**Quero** me cadastrar no sistema informando meus dados pessoais (nome, CPF, RG, endereço e profissão)  
**Para** poder acessar os serviços de aluguel de automóveis

**Critérios de Aceitação:**
- O sistema deve validar que o CPF não está duplicado
- Todos os campos obrigatórios devem ser preenchidos
- O cadastro retorna HTTP 201 com os dados do cliente criado

---

### US02 — Atualizar dados cadastrais
**Como** cliente cadastrado  
**Quero** atualizar meus dados de cadastro  
**Para** manter minhas informações sempre corretas no sistema

**Critérios de Aceitação:**
- Somente o cliente com o ID informado tem seus dados alterados
- O CPF não pode conflitar com o de outro cliente existente

---

### US03 — Consultar dados cadastrais
**Como** cliente cadastrado  
**Quero** consultar meus dados de cadastro  
**Para** verificar as informações armazenadas no sistema

**Critérios de Aceitação:**
- O sistema retorna os dados do cliente pelo ID informado
- Retorna 404 caso o cliente não seja encontrado

---

### US04 — Remover cadastro
**Como** cliente cadastrado  
**Quero** solicitar a exclusão do meu cadastro  
**Para** encerrar meu vínculo com o sistema

**Critérios de Aceitação:**
- O sistema remove o cadastro do cliente pelo ID informado
- Retorna 204 (No Content) em caso de sucesso
- Retorna 404 caso o cliente não exista

---

## Épico 2: Consulta de Automóveis

### US05 — Consultar automóveis disponíveis
**Como** cliente cadastrado  
**Quero** visualizar a lista de automóveis disponíveis para aluguel  
**Para** escolher o veículo que desejo alugar

**Critérios de Aceitação:**
- O sistema lista todos os automóveis com o campo `disponivel = true`
- Cada automóvel exibe: marca, modelo, ano, placa e status de disponibilidade
- É possível listar todos os automóveis sem filtro ou apenas os disponíveis via `?disponivel=true`

---

### US06 — Consultar detalhes de um automóvel
**Como** cliente cadastrado  
**Quero** ver os detalhes de um automóvel específico  
**Para** tomar uma decisão informada antes de criar o pedido

**Critérios de Aceitação:**
- O sistema retorna os dados completos do automóvel pelo ID informado (matrícula, ano, marca, modelo, placa, disponibilidade)
- Retorna 404 caso o automóvel não seja encontrado

---

## Épico 3: Pedidos de Aluguel

### US07 — Criar pedido de aluguel
**Como** cliente cadastrado  
**Quero** criar um pedido de aluguel informando o automóvel desejado e o período  
**Para** iniciar o processo de locação de um veículo

**Critérios de Aceitação:**
- O cliente deve estar cadastrado no sistema
- O automóvel deve estar disponível (`disponivel = true`)
- A data de fim deve ser posterior à data de início
- O pedido é criado com status `PENDENTE`
- O automóvel é marcado como indisponível após a criação do pedido
- O sistema retorna HTTP 201 com os dados completos do pedido

---

### US08 — Consultar status de um pedido
**Como** cliente cadastrado  
**Quero** visualizar o status atual do meu pedido de aluguel  
**Para** acompanhar o andamento da minha solicitação

**Critérios de Aceitação:**
- O sistema retorna os dados completos do pedido pelo ID, incluindo o status atual
- O status pode ser: PENDENTE, EM_ANALISE, APROVADO, REJEITADO, CANCELADO ou CONCLUIDO
- Retorna 404 caso o pedido não seja encontrado

---

### US09 — Listar meus pedidos
**Como** cliente cadastrado  
**Quero** visualizar todos os meus pedidos de aluguel  
**Para** ter um histórico completo das minhas solicitações

**Critérios de Aceitação:**
- O sistema filtra pedidos pelo `clienteId` informado via query param `?clienteId=`
- Retorna lista vazia caso o cliente não possua pedidos

---

### US10 — Cancelar pedido
**Como** cliente cadastrado  
**Quero** cancelar um pedido que ainda está em aberto  
**Para** desistir de uma solicitação antes de ser analisada

**Critérios de Aceitação:**
- Somente pedidos com status `PENDENTE` podem ser cancelados pelo cliente
- Após o cancelamento, o automóvel volta a ficar disponível
- O status do pedido é atualizado para `CANCELADO`
- Retorna 422 se o pedido não estiver em status cancelável

---

## Épico 4: Análise de Pedidos por Agentes

### US11 — Iniciar análise de pedido
**Como** agente (empresa ou banco)  
**Quero** mover um pedido para o estado de análise financeira  
**Para** avaliar a viabilidade do contrato de aluguel

**Critérios de Aceitação:**
- Somente pedidos com status `PENDENTE` podem ser movidos para `EM_ANALISE`
- A transição inválida retorna erro 422

---

### US12 — Aprovar pedido
**Como** agente  
**Quero** aprovar um pedido após análise financeira positiva  
**Para** autorizar a execução do contrato de aluguel

**Critérios de Aceitação:**
- Somente pedidos com status `EM_ANALISE` podem ser aprovados
- O status é atualizado para `APROVADO`

---

### US13 — Rejeitar pedido
**Como** agente  
**Quero** rejeitar um pedido após análise financeira negativa  
**Para** informar ao cliente que o contrato não foi aprovado

**Critérios de Aceitação:**
- Somente pedidos com status `EM_ANALISE` podem ser rejeitados
- O status é atualizado para `REJEITADO`
- O automóvel volta a ficar disponível após a rejeição

---

### US14 — Concluir pedido aprovado
**Como** agente  
**Quero** marcar um pedido aprovado como concluído  
**Para** registrar a finalização do contrato de aluguel

**Critérios de Aceitação:**
- Somente pedidos com status `APROVADO` podem ser marcados como `CONCLUIDO`

---

## Épico 5: Gestão de Automóveis (Administrativo)

### US15 — Cadastrar automóvel
**Como** administrador  
**Quero** cadastrar um novo automóvel na frota  
**Para** torná-lo disponível para aluguel no sistema

**Critérios de Aceitação:**
- Placa e matrícula devem ser únicas no sistema
- O automóvel é criado como disponível por padrão

---

### US16 — Atualizar dados do automóvel
**Como** administrador  
**Quero** atualizar os dados de um automóvel cadastrado  
**Para** manter as informações da frota corretas

---

### US17 — Remover automóvel
**Como** administrador  
**Quero** remover um automóvel da frota  
**Para** desativar veículos que não serão mais utilizados

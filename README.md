# 🚗 Sistema de Aluguel de Carros

## 📌 Sobre o projeto

O Sistema de Aluguel de Carros tem como objetivo apoiar a gestão completa do processo de aluguel de veículos, permitindo o cadastro de clientes, gerenciamento de automóveis, criação e acompanhamento de pedidos e análise por agentes.

O sistema contempla regras de negócio relacionadas à análise financeira, aprovação de pedidos e geração de contratos, podendo envolver também a concessão de crédito por instituições financeiras.

---

## 🧠 Visão Geral

O sistema é composto por:

- Clientes que solicitam o aluguel de veículos;
- Agentes responsáveis pela análise e decisão sobre os pedidos;
- Bancos que podem conceder crédito associado ao contrato;
- Automóveis disponíveis para aluguel;
- Pedidos que passam por análise e aprovação;
- Contratos gerados após aprovação.

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas inspirada em princípios de Clean Architecture:

- **Controller** → Entrada da aplicação (API REST)
- **Use Case (Application Layer)** → Orquestração dos fluxos
- **Domain** → Regras de negócio e entidades
- **Infrastructure** → Persistência, mapeamentos e configurações

---

## 🧱 Estrutura do Projeto

```bash
backend/
docs/
docker-compose.yml
README.md
```

## 📂 backend/

Código-fonte da aplicação Micronaut.

## 📂 docs/

Documentação completa do sistema:

- requisitos/ → histórias de usuário e regras de negócio
- uml/ → diagramas UML
- arquitetura/ → decisões arquiteturais

## ⚙️ Tecnologias Utilizadas

Java
Micronaut
PostgreSQL
Docker / Docker Compose
PlantUML
VS Code

## 📊 Diagramas

Os diagramas do sistema estão disponíveis em:

```bash
docs/uml/
```

Incluem:

- Diagrama de Casos de Uso
- Diagrama de Classes
- Diagrama de Pacotes
- Diagrama de Componentes
- Diagrama de Implantação

## 📌 Funcionalidades

### 👤 Cliente

Cadastro de cliente
Atualização de dados
Consulta de automóveis
Criação de pedido de aluguel
Consulta de status do pedido
Cancelamento de pedido

### 🧑‍💼 Agente

Consulta de pedidos
Análise de pedido
Aprovação ou rejeição

### 🏦 Banco

Associação de contrato de crédito

## 📋 Histórias de Usuário

As histórias de usuário completas estão disponíveis em:

```bash
docs/requisitos/historias-de-usuario.md
```

## 📜 Regras de Negócio

Disponíveis em:

```bash
docs/requisitos/regras-de-negocio.md
```

## 🐳 Execução com Docker

### Subir a aplicação

```bash
docker compose up --build
```

## 📦 Endpoints (exemplo)

- POST /clientes
- GET /clientes
- POST /pedidos
- GET /pedidos/{id}
- PUT /pedidos/{id}/status

## 👨‍💻 Autor

Gustavo Pessoa Firmino Duarte
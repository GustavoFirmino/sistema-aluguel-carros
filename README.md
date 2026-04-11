# Sistema de Aluguel de Carros

## Sobre o projeto

Sistema para apoio à gestão de aluguéis de automóveis, permitindo efetuar, cancelar e modificar pedidos através de uma API REST. O sistema contempla o cadastro de clientes, gerenciamento da frota de veículos, criação e acompanhamento de pedidos de aluguel e análise por agentes.

Desenvolvido em Java com o framework **Micronaut**, seguindo arquitetura **MVC** e princípios de **Clean Architecture**.

---

## Visão Geral

O sistema é composto por:

- **Clientes** que solicitam o aluguel de veículos
- **Agentes** (empresas e bancos) responsáveis pela análise e decisão sobre os pedidos
- **Automóveis** disponíveis para aluguel na frota
- **Pedidos** que passam por um fluxo de análise e aprovação

---

## Arquitetura

O projeto segue uma arquitetura em camadas inspirada em Clean Architecture:

```
[HTTP Request]
      ↓
[Controller]       — Entrada da API REST
      ↓
[Use Cases]        — Orquestração das regras de negócio
      ↓
[Domain Models]    — Entidades e enums de domínio
      ↓
[Repositories]     — Interface de acesso a dados
      ↓
[JPA Entities]     — Mapeamento com o banco de dados
      ↓
[PostgreSQL]       — Persistência
```

**Camadas:**
- `controller/` — Endpoints REST
- `application/usecase/` — Casos de uso (lógica de negócio)
- `domain/model/` — Entidades e enums de domínio
- `dto/` — Objetos de transferência de dados (entrada/saída)
- `infrastructure/mapper/` — Conversão entre camadas
- `infrastructure/persistence/` — Repositórios e entidades JPA
- `infrastructure/exception/` — Exceções e handler global

---

## Estrutura do Projeto

```
sistema-aluguel-carros/
├── backend/                    # Código-fonte Java/Micronaut
│   └── src/main/java/.../
│       ├── controller/
│       ├── application/usecase/
│       │   ├── cliente/
│       │   ├── automovel/
│       │   └── pedido/
│       ├── domain/model/
│       ├── dto/
│       └── infrastructure/
├── docs/                       # Documentação e diagramas UML
│   ├── requisitos/
│   └── uml/
├── docker-compose.yml
└── README.md
```

---

## Tecnologias Utilizadas

| Tecnologia        | Versão  | Finalidade                     |
|-------------------|---------|-------------------------------|
| Java              | 21      | Linguagem principal            |
| Micronaut         | 4.10.10 | Framework web                  |
| Hibernate JPA     | —       | ORM / persistência             |
| PostgreSQL        | 16      | Banco de dados                 |
| H2                | —       | Banco em memória (dev)         |
| Docker Compose    | —       | Infraestrutura de containers   |
| PlantUML          | —       | Diagramas UML                  |
| Gradle            | 8.x     | Build tool                     |

---

## Diagramas UML

Disponíveis em `docs/uml/`:

| Diagrama             | Arquivo                              |
|----------------------|--------------------------------------|
| Casos de Uso         | `casos-de-uso/caso-de-uso-v1.puml`   |
| Classes              | `classes/classes-v2.puml`            |
| Pacotes              | `pacotes/pacotes-v1.puml`            |
| Componentes          | `componentes/componentes-v2.puml`    |
| Implantação          | `implantacao/implantacao-v1.puml`    |

---

## Endpoints da API

### Sistema

| Método | Endpoint  | Descrição                        |
|--------|-----------|----------------------------------|
| GET    | `/`       | Status da aplicação              |
| GET    | `/health` | Health check da API              |

---

### Clientes — `/clientes`

| Método | Endpoint          | Descrição                        |
|--------|-------------------|----------------------------------|
| POST   | `/clientes`       | Cadastra novo cliente            |
| GET    | `/clientes`       | Lista todos os clientes          |
| GET    | `/clientes/{id}`  | Busca cliente por ID             |
| PUT    | `/clientes/{id}`  | Atualiza dados do cliente        |
| DELETE | `/clientes/{id}`  | Remove cliente                   |

**Corpo de requisição (POST/PUT):**
```json
{
  "nome": "Gustavo Pessoa",
  "cpf": "12345678900",
  "rg": "MG1234567",
  "endereco": "Rua A, 100, Belo Horizonte/MG",
  "profissao": "Engenheiro de Software"
}
```

---

### Automóveis — `/automoveis`

| Método | Endpoint              | Descrição                                         |
|--------|-----------------------|---------------------------------------------------|
| POST   | `/automoveis`         | Cadastra novo automóvel                           |
| GET    | `/automoveis`         | Lista todos os automóveis                         |
| GET    | `/automoveis?disponivel=true` | Lista apenas automóveis disponíveis      |
| GET    | `/automoveis/{id}`    | Busca automóvel por ID                            |
| PUT    | `/automoveis/{id}`    | Atualiza dados do automóvel                       |
| DELETE | `/automoveis/{id}`    | Remove automóvel                                  |

**Corpo de requisição (POST/PUT):**
```json
{
  "matricula": "ABC-001",
  "ano": 2023,
  "marca": "Toyota",
  "modelo": "Corolla",
  "placa": "BRA2E24"
}
```

---

### Pedidos de Aluguel — `/pedidos`

| Método | Endpoint                 | Descrição                                        |
|--------|--------------------------|--------------------------------------------------|
| POST   | `/pedidos`               | Cliente cria novo pedido de aluguel              |
| GET    | `/pedidos`               | Lista todos os pedidos                           |
| GET    | `/pedidos?clienteId=1`   | Lista pedidos de um cliente específico           |
| GET    | `/pedidos/{id}`          | Consulta status de um pedido                     |
| PATCH  | `/pedidos/{id}/status`   | Agente atualiza o status do pedido               |
| PATCH  | `/pedidos/{id}/cancelar` | Cliente cancela um pedido PENDENTE               |

**Corpo de requisição (POST):**
```json
{
  "clienteId": 1,
  "automovelId": 1,
  "dataInicio": "2025-05-01",
  "dataFim": "2025-05-10",
  "observacao": "Viagem a trabalho"
}
```

**Corpo de requisição (PATCH /status):**
```json
{
  "status": "EM_ANALISE",
  "observacao": "Iniciando análise financeira."
}
```

**Fluxo de status do pedido:**
```
PENDENTE → EM_ANALISE → APROVADO → CONCLUIDO
         ↘ CANCELADO  ↘ REJEITADO
```

---

## Execução com Docker

### Pré-requisitos
- Docker e Docker Compose instalados

### Subir o banco de dados PostgreSQL

```bash
docker compose up -d
```

### Executar a aplicação (modo desenvolvimento — H2 em memória)

```bash
cd backend
./gradlew run
```

A aplicação estará disponível em: `http://localhost:8080`

### Executar com PostgreSQL

Certifique-se que o Docker Compose está rodando e configure o `application.properties` para apontar ao PostgreSQL (descomentando as linhas correspondentes).

---

## Exemplos de uso com curl

### Criar Cliente
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Gustavo Pessoa","cpf":"12345678900","rg":"MG1234567","endereco":"Rua A, 100","profissao":"Engenheiro"}'
```

### Criar Automóvel
```bash
curl -X POST http://localhost:8080/automoveis \
  -H "Content-Type: application/json" \
  -d '{"matricula":"ABC-001","ano":2023,"marca":"Toyota","modelo":"Corolla","placa":"BRA2E24"}'
```

### Criar Pedido de Aluguel
```bash
curl -X POST http://localhost:8080/pedidos \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"automovelId":1,"dataInicio":"2025-05-01","dataFim":"2025-05-10"}'
```

### Consultar Status do Pedido
```bash
curl http://localhost:8080/pedidos/1
```

### Cancelar Pedido
```bash
curl -X PATCH http://localhost:8080/pedidos/1/cancelar
```

### Agente atualiza status do pedido
```bash
curl -X PATCH http://localhost:8080/pedidos/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"EM_ANALISE","observacao":"Iniciando análise."}'
```

---

## Progresso das Sprints

| Sprint   | Entregável                                                         | Status      |
|----------|--------------------------------------------------------------------|-------------|
| Lab02S01 | Diagrama de Casos de Uso, Histórias de Usuário, Classes, Pacotes   | Concluído   |
| Lab02S02 | Revisão de diagramas + Componentes + CRUD de Cliente               | Concluído   |
| Lab02S03 | Revisão de diagramas + Implantação + Protótipo com Pedidos         | Concluído   |

---

## Autor

Gustavo Pessoa Firmino Duarte  
PUC Minas — Engenharia de Software  
Laboratório de Desenvolvimento de Software — 4º Período

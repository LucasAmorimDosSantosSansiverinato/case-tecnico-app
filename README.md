# Backend — desafioTecnico

## Posição na Arquitetura

```
Frontend → BFF → [Backend] → PostgreSQL
```

O Backend é o núcleo de negócio. Único serviço que lê e escreve no banco. O BFF nunca acessa o banco diretamente.

Toda requisição ao Backend é autenticada via **service token JWT** — apenas o BFF pode chamar estes endpoints.

## Stack

- Java 21 / Spring Boot 3.2 / Maven multi-módulo
- PostgreSQL — schema gerenciado pelo **ORM (Hibernate)** com `ddl-auto: update`
- jjwt 0.12.6

## Módulos

| Módulo | Responsabilidade |
|---|---|
| `desafioTecnico-domain` | Entidade `Pessoa` com anotações JPA, interfaces de repositório e exceções |
| `desafioTecnico-application` | Handlers, commands, queries, DTOs e interfaces de porta |
| `desafioTecnico-ioc` | Registra os beans e resolve implementações das interfaces |
| `desafioTecnico-infra-data` | JPA, repositório, cache em memória (ConcurrentHashMap + Semaphore) |
| `desafioTecnico-infra-utils` | Validador de CPF, gerador de login, integração ViaCEP |
| `desafioTecnico-webui` | Controllers REST, ServiceTokenFilter, ponto de entrada |

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/v1/persons` | Cadastra pessoa (endereço preenchido via ViaCEP) |
| `GET` | `/api/v1/persons` | Lista todas as pessoas |
| `GET` | `/api/v1/persons/{id}` | Busca por ID |
| `GET` | `/api/v1/persons/login/{login}` | Busca por login — exclusivo para autenticação do BFF |

> Todos os endpoints exigem `X-Service-Token: <jwt>` assinado com `SERVICE_TOKEN_SECRET`.

## Segurança (JWT)

- **ServiceTokenFilter** valida `X-Service-Token` em toda requisição da API
- Token tem validade de 30 segundos e é gerado pelo BFF a cada chamada
- Sem token ou token inválido → HTTP 403

## Hospedagem

Produção: **Railway** — deploy automático via GitHub Actions no push para `main`.

## Como Rodar Localmente

> Comece pelo projeto **Case-Tecnico** (migration) que sobe o banco e roda o Flyway.

```bash
# 1. Sobe banco e migrations
cd Case-Tecnico && docker compose up postgres migrations

# 2. Sobe o backend
cd case-tecnico-app/backend && mvn spring-boot:run -pl desafioTecnico-webui
```

Disponível em `http://localhost:8080`

## Variáveis de Ambiente

| Variável | Descrição | Padrão local |
|---|---|---|
| `DATABASE_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/desafiotecnico` |
| `DATABASE_USERNAME` | Usuário do banco | `postgres` |
| `DATABASE_PASSWORD` | Senha do banco | `postgres` |
| `SERVICE_TOKEN_SECRET` | Segredo compartilhado com o BFF (mín. 32 chars) | padrão inseguro |

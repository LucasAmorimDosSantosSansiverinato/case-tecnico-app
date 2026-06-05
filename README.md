# Backend — desafioTecnico

## Arquitetura

```
Frontend → BFF → [Backend] → PostgreSQL
```

Núcleo de negócio. Só o BFF chama o Backend — todo endpoint exige service token JWT.

---

## Stack

- Java 21 / Spring Boot 3.2 / Maven multi-módulo
- Hibernate `ddl-auto: update`
- jjwt 0.12.6

---

## Módulos

| Módulo | O que faz |
|---|---|
| `domain` | Entidade `Pessoa`, interfaces de repositório, exceções |
| `application` | Handlers, commands, queries, DTOs |
| `ioc` | Registro de beans |
| `infra-data` | JPA, repositório, cache em memória |
| `infra-utils` | Validador de CPF, gerador de login, ViaCEP |
| `webui` | Controllers REST, filtro de service token, main |

---

## Decisões

**Cache em memória (ConcurrentHashMap):** a geração de login exige checar unicidade antes de persistir. Bater no banco a cada tentativa seria caro, especialmente quando há colisões. O cache carrega todos os logins na inicialização e mantém sincronizado — verificação vira O(1) em memória.

**Semaphore (1 permissão) no bloco de cadastro:** sem isso, duas threads simultâneas poderiam passar pela verificação de unicidade ao mesmo tempo, gerar o mesmo login e uma delas quebrar na constraint UNIQUE do banco. O Semaphore serializa só o trecho crítico (checar + inserir no cache + escrever no banco), sem travar o resto do servidor.

**Map em vez de Set para o cache:** `ConcurrentHashMap<String, Boolean>` permite leitura concorrente sem lock — múltiplas threads consultam o cache ao mesmo tempo sem problema. Só a escrita vai pelo Semaphore.

**Validação de CPF:** algoritmo dos dígitos verificadores — não valida só o formato, mas se os dígitos batem matematicamente.

---

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/v1/persons` | Cadastra pessoa |
| `GET` | `/api/v1/persons` | Lista todas |
| `GET` | `/api/v1/persons/{id}` | Busca por ID |
| `GET` | `/api/v1/persons/login/{login}` | Busca por login (BFF usa no auth) |

> Todos exigem `X-Service-Token` JWT válido.

---

## Hospedagem

Render — deploy automático no push para `main`.

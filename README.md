# Spring Template

## Visão Geral

Este projeto é um template de API REST em Spring Boot. Ele segue boas práticas de arquitetura, testes automatizados, versionamento de API e integração contínua.

## Estrutura do Projeto

```
src/
 ├── main/
 │    ├── java/com/pablords/spring_template/
 │    │    ├── controller/UserController.java
 │    │    ├── service/UserService.java
 │    │    ├── repository/UserRepository.java
 │    │    └── model/User.java
 │    └── resources/
 │         └── application.yml
 └── test/
      ├── java/com/pablords/spring_template/
      │    ├── component
      │    ├── integration
      │    └── contract
      └── resources/features/
           ├── requests/
           └── user/
```

---

## Tecnologias

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- H2 Database (testes)
- Pact (testes de contrato)
- JUnit 5

---

## Como rodar

1. **Pré-requisitos:** Java 17+, Maven ou Gradle.
2. **Rodar localmente:**
    ```sh
    ./mvnw spring-boot:run
    ```
3. **Testes:**
    ```sh
    ./mvnw test
    ```

---

## Testes

- **Testes de integração:** Validam os fluxos principais da API.
- **Testes de contrato:** Pact garante compatibilidade entre consumer e provider.
- **Testes de componentes:** Validam componentes internos sem testar APIs externas

---

## Convenções e Boas Práticas

- Validações retornam HTTP 422 com mensagem clara.
- Versionamento de API via `/api/v1/`.
- Código organizado em camadas: Controller, Service, Repository, Model.

---

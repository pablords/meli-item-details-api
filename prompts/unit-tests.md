
# Prompt-base (use como mensagem inicial para a IA gerar os testes)

Você é um Engenheiro de Software Sênior. Gere **testes com Spring Boot** para a **camada *application*** (services/use-cases) do meu projeto (Java 17, Spring Boot 3.x).

**Requisitos dos testes**

* Não use Spring (sem **@SpringBootTest**, sem contexto), não faça I/O.
* Use **`@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)`** para subir **apenas o contexto**, sem servidor HTTP.
* **Não** use Testcontainers, banco de dados, nem I/O real.
* **Mocke as portas** (ex.: `ProductRepository`) com **`@MockBean`** para isolar os services do *application*.
* Os services devem ser obtidos via **`@Autowired`** (são beans definidos no meu `AppConfig`).
* Estilo **AAA (Arrange–Act–Assert)**, nomes de teste descritivos (`should...`, `when...then...`).
* Cubra:

   1. **Happy path** (resultado correto)
   2. **Error path** (ex.: `NotFoundException`, validações)
   3. **Interações** com as dependências (`verify(...)`, `verifyNoMoreInteractions(...)`)
   4. **Fronteiras** (ex.: `limit/offset`) via `@ParameterizedTest` quando fizer sentido
   5. Organize os testes com **@Nested** e **@DisplayName** para separar cenários:
      - @Nested class HappyPath { ... }
      - @Nested class ErrorPath { ... }
      - @Nested class Boundaries { ... } (inclua @ParameterizedTest + @MethodSource)
 * Use **`@ActiveProfiles("unit-test")`** e, se necessário, **`@TestPropertySource`** para sobrescrever propriedades.

 **Contexto do projeto**

 * Linguagem: Java 17
 * Arquitetura: Hexagonal (application chama portas do domain)
 * Classe principal: `com.pablords.meli.itemdetail.Application`
 * Config de beans: `com.pablords.meli.itemdetail.config.BeanConfiguration`


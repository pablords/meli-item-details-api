package com.pablords.spring_template.contract;

import au.com.dius.pact.provider.junit5.*;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.pablords.spring_template.handler.GlobalExceptionHandler;
import com.pablords.spring_template.model.User;
import com.pablords.spring_template.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("SpringTemplateService")
// Caminho para os contratos "mockados"
@PactFolder("consumer-api/pacts")
// @PactBroker(url = "http://localhost:9292", // URL do Pact Broker
// authentication = @PactBrokerAuth(username = "admin", password = "password")
// // Se necessário autenticação
// )
@ActiveProfiles("contract-test")
class ProviderContractTest {

  @LocalServerPort
  int port;

  @MockBean
  private UserRepository userRepositoryMock; // Mock do repositório

  @MockBean
  private GlobalExceptionHandler exceptionHandler;

  User user;

  Clock checkoutFixedClock, exceptionFixedClock, checkinFixedClock;
  ZoneId zoneId = ZoneId.of("UTC");

  @BeforeEach
  void setup() {

    user = new User();
    user.setId("123e4567-e89b-12d3-a456-426614174000");
    user.setName("jhon");
    user.setEmail("jhon@eamil.com");

    exceptionFixedClock = Clock.fixed(
        LocalDateTime.parse("2015-01-01T13:00:00").atZone(zoneId).toInstant(),
        zoneId);
  }

  @State("Usuário não existe")
  public void setupUserNotExists() {
    System.out.println("Configurando o estado: Usuário não existe");
    when(userRepositoryMock.findByEmail(user.getEmail()))
        .thenReturn(Optional.empty()); // Simula que não existe usuário com o email fornecido
    //
    when(userRepositoryMock.save(any(User.class)))
        .thenReturn(user); // Simula o retorno do usuário salvo no repositório
  }

  @State("Usuário já existe")
  public void setupUserExists() {
    System.out.println("Configurando o estado: Usuário já existe");
    when(userRepositoryMock.findByEmail(any()))
        .thenReturn(Optional.of(user)); // Simula que já existe um usuário com o email fornecido
    //

    when(exceptionHandler.handleRuntimeException(any(IllegalArgumentException.class), any(HttpServletRequest.class)))
        .thenAnswer(invocation -> {
          HttpServletRequest request = invocation.getArgument(1);
          return new GlobalExceptionHandler(exceptionFixedClock).handleRuntimeException(invocation.getArgument(0),
              request);
        });
  }

  @State("Usuário inválido (sem nome)")
  public void setupInvalidUser() {
    System.out.println("Configurando o estado: Usuário inválido (sem nome)");
    when(exceptionHandler.handleValidationExceptions(any(), any()))
        .thenAnswer(invocation -> {
          return new GlobalExceptionHandler(exceptionFixedClock).handleValidationExceptions(invocation.getArgument(0),
              invocation.getArgument(1));
        });
  }

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void validatePactContract(PactVerificationContext context) {
    context.setTarget(new HttpTestTarget("localhost", port));
    context.verifyInteraction();
  }
}
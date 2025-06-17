package com.pablords.spring_template.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import com.pablords.spring_template.model.User;
import com.pablords.spring_template.repository.UserRepository;
import com.pablords.spring_template.service.UserService;

class UserServiceUnitTest {
  private UserService userService;
  private UserRepository userRepositoryMock;
  User user;

  @BeforeEach
  void setUp() {
    userRepositoryMock = mock(UserRepository.class); // Criando o mock do repositório
    userService = new UserService(userRepositoryMock); // Injetando o mock
    user = new User();
    user.setName("jhon");
    user.setEmail("jhon@eamil.com");
  }

  @Nested
  @DisplayName("Testes para criação de usuário")
  class CreateUserTest {

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void should_be_create_a_user_with_success() {
      when(userRepositoryMock.findByEmail(user.getEmail())).thenReturn(Optional.empty());
      user.setId("12345");
      when(userRepositoryMock.save(user)).thenReturn(user);

      User createdUser = userService.registerUser(user);
      assertNotNull(createdUser);
      assertEquals("12345", createdUser.getId());
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o email já existir")
    void should_throw_exception_when_email_already_exists() {
      when(userRepositoryMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        userService.registerUser(user);
      });

      String expectedMessage = "A email already exists: " + user.getEmail();
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
      verify(userRepositoryMock, never()).save(any(User.class));
    }

  }
}

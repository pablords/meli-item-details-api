package com.pablords.meli_item_details_api.integration.CT001;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pablords.meli_item_details_api.adapters.inbound.http.dto.UserResponseDto;
import com.pablords.meli_item_details_api.adapters.inbound.http.handler.ApiError;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import io.cucumber.java.Before;
import io.cucumber.java.es.Dado;
import io.cucumber.java.it.Quando;
import io.cucumber.java.pt.Entao;

public class UserSteps {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private HttpStatus responseStatus;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  private String responseContent;
  private final String SPRING_TEMPLATE_API_URL_USERS = "/users";
  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Before
  public void setUp() {
    // Limpando a tabelas do banco H2
    jdbcTemplate.execute("DELETE FROM users");

    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Dado("que estou no endpoint da API {string}")
  public void that_i_am_in_the_api_endpoint(String endpoint) {
    assertEquals(endpoint, SPRING_TEMPLATE_API_URL_USERS);
  }

  @Quando("eu crio um usuário com os seguintes detalhes: {string}")
  public void i_create_a_car_with_the_following_details(String jsonPath) throws Exception {
    var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));

    mockMvc.perform(post(SPRING_TEMPLATE_API_URL_USERS)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonFileContent))
        .andExpect(result -> {
          responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
          responseContent = result.getResponse().getContentAsString();
        });
  }

  @Entao("o status da resposta do usuário deve ser {int}")
  public void the_response_status_should_be(int status) throws Exception {
    UserResponseDto userResponseDto = objectMapper.readValue(responseContent, UserResponseDto.class);
    ApiError error = objectMapper.readValue(responseContent, ApiError.class);

    switch (HttpStatus.valueOf(status)) {
      case CREATED:
        assertNotNull(userResponseDto.getId());
        assertEquals("jhon", userResponseDto.getName());
        assertEquals("jhon@email.com", userResponseDto.getEmail());
        assertEquals(status, responseStatus.value());
        break;
      case UNPROCESSABLE_ENTITY:
        assertNotNull(error.getErrors());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), error.getError());
        assertEquals("Validation error", error.getMessage());
        assertEquals("Name cannot be empty", error.getErrors().get("name"));
        assertEquals(status, responseStatus.value());
        break;
      default:
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseStatus.value());
        break;
    }
  }
}

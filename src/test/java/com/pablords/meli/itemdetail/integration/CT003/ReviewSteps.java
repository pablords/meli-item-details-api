package com.pablords.meli.itemdetail.integration.CT003;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pablords.meli.itemdetail.utils.StringCountList;

import io.cucumber.java.pt.*;

public class ReviewSteps {
  @Autowired
  private MockMvc mockMvc;

  private HttpStatus responseStatus;
  private String responseContent;
  private String REVIEW_API_URL;

  @Dado("que estou no endpoint da API de avaliações {string} com id {string}")
  public void que_estou_no_endpoint_da_api_com_id(String endpoint, String id) {
    this.REVIEW_API_URL = endpoint.replace("{id}", id);
  }

  @Quando("eu solicito as avaliações de produtos para o id {string}")
  public void eu_solicito_as_avaliacoes_para_o_id(String id) throws Exception {
    this.REVIEW_API_URL = "/reviews/products/" + id;
    System.out.println("Fazendo chamada para URL: " + REVIEW_API_URL);
    mockMvc.perform(get(REVIEW_API_URL)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(result -> {
          responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
          responseContent = result.getResponse().getContentAsString();
          System.out.println("Status: " + responseStatus + ", Content: " + responseContent);
        });
  }

  @Entao("o status da resposta de avaliações deve ser {int}")
  public void o_status_da_resposta_deve_ser(Integer statusCode) {
    System.out.println("Response data: " + responseContent);

    switch (HttpStatus.valueOf(statusCode)) {
      case OK:
        assertEquals(HttpStatus.OK, responseStatus);
        break;
      default:
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseStatus.value());
        break;
    }
  }

  @E("a resposta deve conter uma lista de avaliações de produtos com {int} itens")
  public void a_resposta_deve_conter_uma_lista_de_avaliacoes_de_produtos(int expectedItemCount) {
    int actualItemCount = StringCountList.countItemsSimple(responseContent);
    assertEquals(expectedItemCount, actualItemCount);
    System.out.println("✅ Expected " + expectedItemCount + " items, found " + actualItemCount);
  }

  @E("a resposta deve conter uma lista vazia de avaliações de produtos")
  public void a_resposta_deve_conter_uma_lista_vazia_de_avaliacoes_de_produtos() {
    int actualItemCount = StringCountList.countItemsSimple(responseContent);
    assertEquals(0, actualItemCount);
    System.out.println("✅ Expected empty list, found " + actualItemCount + " items");
  }

}

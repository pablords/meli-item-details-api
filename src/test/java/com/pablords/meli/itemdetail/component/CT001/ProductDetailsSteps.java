package com.pablords.meli.itemdetail.component.CT001;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pablords.meli.itemdetail.adapters.outbound.file.FileProductRepositoryAdapter;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.valueobject.Money;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;

public class ProductDetailsSteps {
  @Autowired
  private MockMvc mockMvc;
  private HttpStatus responseStatus;
  private String responseContent;
  private String PRODUCT_DETAILS_API_URL = "/products";
  @Autowired
  private FileProductRepositoryAdapter fileProductRepository;

  @Before
  public void setUp() {
    Product product = Product.create(
        "12345",
        "Smartphone XYZ",
        "BrandA",
        "Electronics",
        new Money(999.99, "USD"),
        "http://example.com/thumbnail.jpg",
        List.of("http://example.com/pic1.jpg", "http://example.com/pic2.jpg"),
        Map.of("color", "black", "memory", "128GB"),
        50,
        UUID.randomUUID().toString());

    when(fileProductRepository.getById("12345")).thenReturn(Optional
        .of(product));
    when(fileProductRepository.getById("99999")).thenReturn(Optional.empty());
  }

  @Dado("que estou no endpoint da API {string} com id {string}")
  public void que_estou_no_endpoint_da_api_com_id(String endpoint, String id) {
    this.PRODUCT_DETAILS_API_URL = endpoint;
    assertEquals(endpoint, PRODUCT_DETAILS_API_URL);
    assertEquals(id, "12345");
  }

  @Quando("eu solicito os detalhes do produto com id {string}")
  public void eu_solicito_os_detalhes_do_produto(String id) throws Exception {
    this.PRODUCT_DETAILS_API_URL = "/products" + "/" + id;
    mockMvc.perform(get(PRODUCT_DETAILS_API_URL)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(result -> {
          responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
          responseContent = result.getResponse().getContentAsString();
        });
  }

  @Entao("o status da resposta deve ser {int}")
  public void o_status_da_resposta_deve_ser(Integer statusCode) {
    System.out.println("Response data: " + responseContent);

    switch (HttpStatus.valueOf(statusCode)) {
      case OK:
        assertEquals(HttpStatus.OK, responseStatus);
        break;
      case NOT_FOUND:
        assertEquals(HttpStatus.NOT_FOUND, responseStatus);
        assertEquals("{\"message\":\"Product not found\"}", responseContent);
        break;
      default:
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseStatus.value());
        break;
    }
  }

}

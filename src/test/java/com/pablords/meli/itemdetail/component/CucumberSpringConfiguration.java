package com.pablords.meli.itemdetail.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.meli.itemdetail.adapters.outbound.file.FileProductRepositoryAdapter;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.valueobject.Money;
import com.pablords.meli.itemdetail.domain.valueobject.Seller;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("component-test")
public class CucumberSpringConfiguration {

  @MockBean
  private FileProductRepositoryAdapter fileProductRepositoryAdapter;

  @Autowired
  private Environment environment;

  @Before
  public void setUp() {
    System.out.println("🔎 Active Profiles: " + String.join(", ", environment.getActiveProfiles()));

    // Configurar mocks para os testes
    Product product1 = Product.create("12345",
        "Smartphone XYZ",
        "BrandA",
        "Electronics",
        new Money(99.99, "USD"),
        "http://example.com/thumbnail.jpg",
        List.of("http://example.com/pic1.jpg", "http://example.com/pic2.jpg"),
        Map.of("color", "black", "memory", "128GB"),
        10,
        "seller123");

    Product product2 = Product.create(
        "54321",
        "Smartphone XYZ",
        "BrandA",
        "Electronics",
        new Money(99.99, "USD"),
        "http://example.com/thumbnail.jpg",
        List.of("http://example.com/pic1.jpg", "http://example.com/pic2.jpg"),
        Map.of("color", "black", "memory", "128GB"),
        10,
        "seller123");

    Seller seller = new Seller("seller123", "vendedor_exemplo", 4.5);

    when(fileProductRepositoryAdapter.getById("12345")).thenReturn(Optional.of(product1));
    when(fileProductRepositoryAdapter.getById("99999")).thenReturn(Optional.empty());
    when(fileProductRepositoryAdapter.getSellerById("seller123")).thenReturn(Optional.of(seller));

    List<Product> products = List.of(product1, product2);
    when(fileProductRepositoryAdapter.recommendations("12345", 6)).thenReturn(products);
    when(fileProductRepositoryAdapter.recommendations("99999", 6)).thenReturn(List.of());

    System.out.println("✅ Mocks configurados no CucumberSpringConfiguration");
  }
}
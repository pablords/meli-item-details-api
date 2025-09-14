package com.pablords.meli.itemdetail.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.meli.itemdetail.adapters.outbound.file.FileProductRepositoryAdapter;
import com.pablords.meli.itemdetail.adapters.outbound.file.FileReviewRepositoryAdapter;
import com.pablords.meli.itemdetail.domain.entity.Product;
import com.pablords.meli.itemdetail.domain.entity.Review;
import com.pablords.meli.itemdetail.domain.valueobject.Money;
import com.pablords.meli.itemdetail.domain.valueobject.Paged;
import com.pablords.meli.itemdetail.domain.valueobject.ReviewSort;
import com.pablords.meli.itemdetail.domain.valueobject.Seller;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("component-test")
public class CucumberSpringConfiguration {

  private Paged<Review> paged;
  private List<Review> items;

  @MockBean
  private FileProductRepositoryAdapter fileProductRepositoryAdapter;
  @MockBean
  private FileReviewRepositoryAdapter fileReviewRepositoryAdapter;

  @Autowired
  private Environment environment;

  @Before
  public void setUp() {
    System.out.println("🔎 Active Profiles: " + String.join(", ", environment.getActiveProfiles()));

    // Configurar mocks para os testes
    Product product1 = Product.create("MLB001",
        "Smartphone Samsung Galaxy S24 Ultra 256GB",
        "Samsung",
        "Celulares",
        new Money(4999.99, "BRL"),
        "https://http2.mlstatic.com/D_Q_NP_123456_MLB.jpg",
        List.of("https://http2.mlstatic.com/D_Q_NP_123456_MLB.jpg", "https://http2.mlstatic.com/D_Q_NP_123457_MLB.jpg"),
        Map.of("Memoria", "256GB"),
        25,
        "SELLER001");

    Product product2 = Product.create("MLB002",
        "iPhone 15 Pro Max 256GB Titânio Natural",
        "Apple",
        "Celulares",
        new Money(8999.99, "BRL"),
        "https://http2.mlstatic.com/D_Q_NP_223456_MLB.jpg",
        List.of("https://http2.mlstatic.com/D_Q_NP_223456_MLB.jpg", "https://http2.mlstatic.com/D_Q_NP_223457_MLB.jpg"),
        Map.of("Memoria", "256GB"),
        15,
        "SELLER002");

    Product product3 = Product.create("MLB003",
        "iPhone 15 Pro Max 256GB Titânio Natural",
        "Apple",
        "Celulares",
        new Money(8999.99, "BRL"),
        "https://http2.mlstatic.com/D_Q_NP_223456_MLB.jpg",
        List.of("https://http2.mlstatic.com/D_Q_NP_223456_MLB.jpg", "https://http2.mlstatic.com/D_Q_NP_223457_MLB.jpg"),
        Map.of("Memoria", "256GB"),
        15,
        "SELLER002");

    Seller seller = new Seller("SELLER001", "TechMaster_SP", 4.8);

    when(fileProductRepositoryAdapter.getById("MLB001")).thenReturn(Optional.of(product1));
    when(fileProductRepositoryAdapter.getById("99999")).thenReturn(Optional.empty());
    when(fileProductRepositoryAdapter.getSellerById("SELLER001")).thenReturn(Optional.of(seller));

    List<Product> products = List.of(product1, product2, product3);
    when(fileProductRepositoryAdapter.recommendations("MLB001", 6)).thenReturn(products);
    when(fileProductRepositoryAdapter.recommendations("99999", 6)).thenReturn(List.of());

    var review1 = Review.create("1", "MLB001", 3, "product1", "first", "author1", false, 0,
        LocalDateTime.now().toString(),
        "pt-BR");
    var review2 = Review.create("2", "MLB001", 4, "product1", "second", "author2", false, 0,
        LocalDateTime.now().toString(),
        "pt-BR");

    items = List.of(review1, review2);

    when(fileReviewRepositoryAdapter.findByProduct("MLB001", ReviewSort.RECENT, 2, 0)).thenReturn(items);
    when(fileReviewRepositoryAdapter.totalByProduct("MLB001")).thenReturn(2);

    System.out.println("✅ Mocks configurados no CucumberSpringConfiguration");
  }
}
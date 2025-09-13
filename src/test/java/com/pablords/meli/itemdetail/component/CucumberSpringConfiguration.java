package com.pablords.meli.itemdetail.component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.meli.itemdetail.adapters.outbound.file.FileProductRepositoryAdapter;
import com.pablords.meli.itemdetail.domain.ports.outbound.repository.ProductRepositoryPort;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("component-test")
public class CucumberSpringConfiguration {

  @MockBean
  private FileProductRepositoryAdapter fileProductRepository;

  @MockBean
  private ProductRepositoryPort productRepository;

  @Autowired
  private Environment environment;

  @Before
  public void setUp() {
    System.out.println("🔎 Active Profiles: " + String.join(", ", environment.getActiveProfiles()));
  }
}
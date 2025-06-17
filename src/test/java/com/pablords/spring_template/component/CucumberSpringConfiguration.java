package com.pablords.spring_template.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.spring_template.repository.UserRepository;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("component-test")
public class CucumberSpringConfiguration {
  @MockBean
  private UserRepository userRepositoryMock;


  @Autowired
  private Environment environment;

  @Before
  public void setUp() {
    System.out.println("🔎 Active Profiles: " + String.join(", ", environment.getActiveProfiles()));
  }
}
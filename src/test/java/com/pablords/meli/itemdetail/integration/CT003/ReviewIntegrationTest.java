package com.pablords.meli.itemdetail.integration.CT003;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/reviews", glue = {
    "com.pablords.meli.itemdetail.integration" }, plugin = { "pretty",
        "json:target/cucumber-reports/integration/CT003/report-CT003.json",
        "html:target/cucumber-reports/integration/CT003/report-CT003.html" }, monochrome = true)
public class ReviewIntegrationTest {
  
}

package com.pablords.meli.itemdetail.component.CT002;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/product-recommendations", glue = {
    "com.pablords.meli.itemdetail.component" }, plugin = { "pretty",
        "json:target/cucumber-reports/component/report-CT002.json",
        "html:target/cucumber-reports/component/report-CT002.html" }, monochrome = true)
public class ProductRecommendationsComponentTest {
  
}

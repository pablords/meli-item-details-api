package com.pablords.meli.itemdetail.integration.CT001;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/product-details", glue = {
    "com.pablords.meli.itemdetail.integration" }, plugin = {
        "pretty", "json:target/cucumber-reports/integration/report-CT001.json",
        "html:target/cucumber-reports/integration/report-CT001.html" }, monochrome = true)
public class ProductDetailsIntegrationTest {

}

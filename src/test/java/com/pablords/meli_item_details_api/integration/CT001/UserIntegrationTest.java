package com.pablords.meli_item_details_api.integration.CT001;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/user", glue = {
    "com.pablords.spring_template.integration" }, plugin = {
        "pretty", "json:target/cucumber-reports/integration/report-CT001.json",
        "html:target/cucumber-reports/integration/report-CT001.html" }, monochrome = true)
@ActiveProfiles("integration-test")
public class UserIntegrationTest {

}

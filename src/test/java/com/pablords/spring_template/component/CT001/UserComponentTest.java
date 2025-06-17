package com.pablords.spring_template.component.CT001;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/user", glue = {
    "com.pablords.spring_template.component" }, plugin = { "pretty",
        "json:target/cucumber-reports/component/report-CT001.json",
        "html:target/cucumber-reports/component/report-CT001.html" }, monochrome = true)
public class UserComponentTest {

}

package de.leanovate.bookdb.blackbox;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber"}, tags = {"~@ignore"},
        glue = {"classpath:de/leanovate/bookdb", "classpath:de/leanovate/cucumber"})
public class RunCukesTest {
}

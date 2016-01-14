package de.leanovate.bookdb.blackbox;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import de.leanovate.cucumber.rest.TestHttpClient;

import java.net.URI;

public class TestConfig {
    public final URI baseUrl;
    public final TestHttpClient client;

    public TestConfig(TestHttpClient client) throws Exception {
        this.baseUrl = new URI("http://localhost:8080");
        this.client = client;
    }

    @Before
    public void setScenario(Scenario scenario) {
        client.setScenario(scenario);
    }

}

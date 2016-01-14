package de.leanovate.bookdb.blackbox;

import de.leanovate.cucumber.rest.TestHttpClient;

import java.net.URI;

public class TestConfig {
    public final URI baseUrl;

    public TestConfig(TestHttpClient client) throws Exception {
        this.baseUrl = new URI("http://localhost:8080");
    }

}

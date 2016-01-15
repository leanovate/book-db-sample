package de.leanovate.bookdb.blackbox;

import cucumber.api.java.en.When;
import de.leanovate.bookdb.blackbox.models.Author;
import de.leanovate.cucumber.rest.JsonHelper;
import de.leanovate.cucumber.rest.TestHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import static de.leanovate.cucumber.rest.RestAssertions.assertThat;

public class AuthorStepdefs {
    private final TestConfig config;
    private final TestHttpClient client;

    public AuthorStepdefs(TestConfig config, TestHttpClient client) {
        this.config = config;
        this.client = client;
    }

    @When("^An author with name \"([^\"]*)\" is added$")
    public void anAuthorWithNameIsAdded(String authorName) throws Throwable {
        Author author = new Author(authorName);
        Request request = Request.Post(config.baseUrl.resolve("/v1/authors")).body(JsonHelper.valueToEntity(author));

        HttpResponse response = client.execute(request);

        assertThat(response).isCreated();
    }
}

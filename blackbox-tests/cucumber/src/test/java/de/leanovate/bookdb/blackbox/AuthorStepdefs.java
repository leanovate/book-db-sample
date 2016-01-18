package de.leanovate.bookdb.blackbox;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.leanovate.bookdb.blackbox.models.Author;
import de.leanovate.cucumber.rest.JsonHelper;
import de.leanovate.cucumber.rest.TestHttpClient;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import java.util.Optional;

import static de.leanovate.cucumber.rest.RestAssertions.assertThat;

public class AuthorStepdefs {
    private final TestConfig config;
    private final TestHttpClient client;

    public Optional<String> authorUri = Optional.empty();
    public Optional<Author> author = Optional.empty();

    public AuthorStepdefs(TestConfig config, TestHttpClient client) {
        this.config = config;
        this.client = client;
    }

    @When("^An author with name \"([^\"]*)\" is added$")
    public void anAuthorWithNameIsAdded(String authorName) throws Throwable {
        Author author = new Author(authorName);
        Request request = Request.Post(config.baseUrl.resolve("/v1/authors")).body(JsonHelper.valueToEntity(author));

        HttpResponse response = client.execute(request);

        assertThat(response).isCreated().hasHeader(HttpHeaders.LOCATION);

        authorUri = Optional.of(response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    }

    @Then("^The created author does exists$")
    public void theCreatedAuthorDoesExists() throws Throwable {
        Request request = Request.Get(authorUri.orElseThrow(() -> new RuntimeException("Author uri not set")));

        author = Optional.of(client.executeJson(request, Author.class));
    }

    @Then("^The author has the name \"([^\"]*)\"$")
    public void theAuthorHasTheName(String authorName) throws Throwable {
        Author actual = author.orElseThrow(() -> new RuntimeException("Author not set"));

        assertThat(actual.name).isEqualTo(authorName);
    }

    @When("^The author is deleted$")
    public void theAuthorIsDeleted() throws Throwable {
        Request request = Request.Delete(authorUri.orElseThrow(() -> new RuntimeException("Author uri not set")));

        HttpResponse response = client.execute(request);

        assertThat(response).isNoContent();
    }

    @Then("^The author does not exists$")
    public void theAuthorDoesNotExists() throws Throwable {
        Request request = Request.Get(authorUri.orElseThrow(() -> new RuntimeException("Author uri not set")));

        HttpResponse response = client.execute(request);

        assertThat(response).isNotFound();
    }
}

package de.leanovate.bookdb.blackbox;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.leanovate.bookdb.blackbox.models.Book;
import de.leanovate.cucumber.rest.JsonHelper;
import de.leanovate.cucumber.rest.TestHttpClient;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.assertj.core.api.Assertions;

import java.util.Optional;

import static de.leanovate.cucumber.rest.RestAssertions.assertThat;

public class BookStepdefs {
    private final TestConfig config;
    private final TestHttpClient client;

    public Optional<String> bookUri = Optional.empty();
    public Optional<Book> book = Optional.empty();

    public BookStepdefs(TestConfig config, TestHttpClient client) {
        this.config = config;
        this.client = client;
    }

    @When("^A book with title \"([^\"]*)\" without author is created$")
    public void aBookWithTitleWithoutAuthorIsCreated(String title) throws Throwable {
        Book book = new Book(title);
        Request request = Request.Post(config.baseUrl.resolve("/v1/books")).body(JsonHelper.valueToEntity(book));

        HttpResponse response = client.execute(request);

        assertThat(response).isCreated().hasHeader(HttpHeaders.LOCATION);

        bookUri = Optional.of(response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    }

    @Then("^The created book does exists$")
    public void theCreatedBookCanBeOpened() throws Throwable {
        Request request = Request.Get(bookUri.orElseThrow(() -> new RuntimeException("Book uri not set")));

        book = Optional.of(client.executeJson(request, Book.class));
    }

    @Then("^The book has the title \"([^\"]*)\"$")
    public void theBookHasTheTitle(String title) throws Throwable {
        Book actual = book.orElseThrow(() -> new RuntimeException("Book not set"));

        Assertions.assertThat(actual.title).isEqualTo(title);
    }

    @When("^The book is deleted$")
    public void theBookIsDeleted() throws Throwable {
        Request request = Request.Delete(bookUri.orElseThrow(() -> new RuntimeException("Book uri not set")));

        HttpResponse response = client.execute(request);

        assertThat(response).isNoContent();
    }

    @Then("^The book does not exists$")
    public void theBookDoesNotExists() throws Throwable {
        Request request = Request.Get(bookUri.orElseThrow(() -> new RuntimeException("Book uri not set")));

        HttpResponse response = client.execute(request);

        assertThat(response).isNotFound();
    }
}

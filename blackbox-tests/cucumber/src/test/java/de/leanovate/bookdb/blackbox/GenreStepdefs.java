package de.leanovate.bookdb.blackbox;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.leanovate.bookdb.blackbox.models.Genre;
import de.leanovate.cucumber.rest.JsonHelper;
import de.leanovate.cucumber.rest.TestHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import static de.leanovate.cucumber.rest.RestAssertions.assertThat;

public class GenreStepdefs {
    private final TestConfig config;
    private final TestHttpClient client;

    public GenreStepdefs(TestConfig config, TestHttpClient client) {
        this.config = config;
        this.client = client;
    }


    @When("^A genre \"([^\"]*)\" is added$")
    public void a_genre_is_added(String genreName) throws Throwable {
        Genre genre = new Genre(genreName, "Description of " + genreName);
        Request request = Request.Post(config.baseUrl.resolve("/v1/genres")).body(JsonHelper.valueToEntity(genre));

        HttpResponse response = client.execute(request);

        assertThat(response).isCreated();
    }

    @Then("^The genre \"([^\"]*)\" exists$")
    public void theGenreExists(String genreName) throws Throwable {
        Request request = Request.Get(config.baseUrl.resolve("/v1/genres/" + genreName));

        HttpResponse response = client.execute(request);

        assertThat(response).isOk();
    }

    @Then("^The genre \"([^\"]*)\" does not exists$")
    public void theGenreDoesNotExists(String genreName) throws Throwable {
        Request request = Request.Get(config.baseUrl.resolve("/v1/genres/" + genreName));

        HttpResponse response = client.execute(request);

        assertThat(response).isNotFound();
    }

    @When("^The genre \"([^\"]*)\" is deleted$")
    public void theGenreIsDeleted(String genreName) throws Throwable {
        Request request = Request.Delete(config.baseUrl.resolve("/v1/genres/" + genreName));

        HttpResponse response = client.execute(request);

        assertThat(response).isNoContent();
    }

}

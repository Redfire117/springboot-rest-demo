package org.grostarin.springboot.demorest.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DeniedSpringBootBootstrapLiveTest {

    @LocalServerPort
    private int port;
       
    private String getApiRoot() {
        return "http://localhost:"+port+"/api/books";
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        final Response response = RestAssured.get(getApiRoot());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetBooksByTitle_thenOK() {
        final DeniedBook deniedbook = createRandomBook();
        createBookAsUri(deniedbook);

        final Response response = RestAssured.get(getApiRoot() + "?title=" + deniedbook.getTitle());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenGetCreatedBookById_thenOK() {
        final DeniedBook deniedbook = createRandomBook();
        final String location = createBookAsUri(deniedbook);

        final Response response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(deniedbook.getTitle(), response.jsonPath()
            .get("title"));
    }

    @Test
    public void whenGetNotExistBookById_thenNotFound() {
        final Response response = RestAssured.get(getApiRoot() + "/" + randomNumeric(4));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    // POST
    @Test
    public void whenCreateNewBook_thenCreated() {
        final DeniedBook deniedbook = createRandomBook();

        final Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedbook)
            .post(getApiRoot());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void whenInvalidBook_thenError() {
        final DeniedBook deniedbook = createRandomBook();
        deniedbook.setAuthor(null);

        final Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedbook)
            .post(getApiRoot());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedBook_thenUpdated() {
        final DeniedBook deniedbook = createRandomBook();
        final String location = createBookAsUri(deniedbook);

        deniedbook.setId(Long.parseLong(location.split("api/books/")[1]));
        deniedbook.setAuthor("newAuthor");
        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedbook)
            .put(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("newAuthor", response.jsonPath()
            .get("author"));

    }

    @Test
    public void whenDeleteCreatedBook_thenOk() {
        final DeniedBook deniedbook = createRandomBook();
        final String location = createBookAsUri(deniedbook);

        Response response = RestAssured.delete(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    // ===============================

    private DeniedBook createRandomBook() {
        final DeniedBook deniedbook = new DeniedBook();
        deniedbook.setTitle(randomAlphabetic(10));
        deniedbook.setAuthor(randomAlphabetic(15));
        return deniedbook;
    }

    private String createBookAsUri(DeniedBook deniedbook) {
        final Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedbook)
            .post(getApiRoot());
        return getApiRoot() + "/" + response.jsonPath()
            .get("id");
    }

}
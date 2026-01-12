package controllers;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

public class ApplicationTest extends WithApplication {

    @Test
    public void test() {
        final Http.RequestBuilder request = new Http.RequestBuilder()
            .method("GET")
            .uri("/");

        final Result result = route(app, request);
        assertNotNull(result);
        assertEquals(200, result.status());
        assertTrue(result.header("X-Response-Time").isPresent());
        assertEquals("text/html", result.header("Content-Type").orElse(null));
        assertThat(contentAsString(result), containsString("body"));
    }

}

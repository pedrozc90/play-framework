package web.controllers.files;

import org.junit.Test;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;
import static play.test.Helpers.status;

/**
 * Regression coverage: GET /api/files must work when the optional `q` query
 * parameter is omitted. Requires a reachable Postgres with evolutions applied
 * (e.g. `docker-compose up -d db`).
 */
public class FilesRoutesTest extends WithApplication {

    @Test
    public void fetchWithoutQueryParamSucceeds() {
        final Result result = route(fakeRequest(GET, "/api/files"));
        assertEquals(200, status(result));
    }

}

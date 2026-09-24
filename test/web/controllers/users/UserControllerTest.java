package web.controllers.users;

import application.users.UserService;
import domain.users.User;
import org.junit.Test;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.status;

/**
 * Regression coverage: GET /api/users/:id must return 200, not 201.
 * Calls the controller method directly (bypassing the @Authenticated action chain,
 * which only applies through real HTTP routing). Requires a reachable Postgres with
 * evolutions applied (e.g. `docker-compose up -d db`).
 */
public class UserControllerTest extends WithApplication {

    private final UserService service = UserService.getInstance();

    @Test
    public void getReturnsOkNotCreated() throws Throwable {
        final String email = "user-" + UUID.randomUUID() + "@example.com";

        JPA.withTransaction(new F.Function0<Void>() {
            @Override
            public Void apply() throws Throwable {
                final User user = service.register(email, "password1");

                final Result result = UserController.get(user.getId());
                assertEquals(200, status(result));

                service.remove(user);
                return null;
            }
        });
    }

}

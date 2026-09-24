package application.users;

import core.exceptions.AppException;
import core.utils.http.HttpStatus;
import domain.users.User;
import org.junit.Test;
import play.db.jpa.JPA;
import play.libs.F;
import play.test.WithApplication;
import web.controllers.users.objects.UserUpdateCmd;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Regression coverage for the UserService.update() password-hashing fix and the
 * UNAUTHORIZED-on-bad-credentials fix. Requires a reachable Postgres with evolutions
 * applied (e.g. `docker-compose up -d db`), same as running the app locally.
 */
public class UserServiceTest extends WithApplication {

    private final UserService service = UserService.getInstance();

    @Test
    public void updatingPasswordHashesItLikeRegistration() throws Throwable {
        final String email = "user-" + UUID.randomUUID() + "@example.com";
        final String initialPassword = "initial1";
        final String newPassword = "updated2";

        JPA.withTransaction(new F.Function0<Void>() {
            @Override
            public Void apply() throws Throwable {
                final User registered = service.register(email, initialPassword);
                assertNotNull(registered.getId());
                assertNotNull(service.get(email, initialPassword));

                service.update(registered.getId(), new UserUpdateCmd(email, newPassword, true));

                assertNotNull(service.get(email, newPassword));
                try {
                    service.get(email, initialPassword);
                    fail("Old password should no longer authenticate after update");
                } catch (AppException e) {
                    assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
                }

                service.remove(registered);
                return null;
            }
        });
    }

}

package repository;

import domain.users.User;
import infrastructure.repositories.users.UserRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import play.db.jpa.JPAApi;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserRepositoryTest extends WithApplication {

    private JPAApi jpa;
    private UserRepositoryImpl repository;

    @Before
    public void setUp() {
        jpa = app.injector().instanceOf(JPAApi.class);
        repository = app.injector().instanceOf(UserRepositoryImpl.class);
    }

    @Test
    public void testFindByEmail() {
        final String email = "junit@email.com";

        final User tmp = new User();
        tmp.setEmail(email);
        tmp.setPassword("password");

        repository.persist(tmp).thenAccept(user -> {
            assertNotNull(user);
            assertEquals(email, user.getEmail());
        });

        repository.get(email).thenAccept((user) -> {
            assertNotNull(user);
            assertEquals(email, user.getEmail());
        });
    }

}

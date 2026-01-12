package repository;

import models.users.User;
import org.junit.Before;
import org.junit.Test;
import play.db.jpa.JPAApi;
import play.test.WithApplication;
import repositories.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserRepositoryTest extends WithApplication {

    private JPAApi jpa;
    private UserRepository repository;

    @Before
    public void setUp() {
        jpa = app.injector().instanceOf(JPAApi.class);
        repository = app.injector().instanceOf(UserRepository.class);
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

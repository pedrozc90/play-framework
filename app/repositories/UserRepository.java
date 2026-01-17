package repositories;

import core.persistence.JpaRepository;
import models.tasks.Task;
import models.users.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UserRepository extends JpaRepository<User, Long> {

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public UserRepository() {
        super(User.class);
    }

    public User get(final String email) {
        try {
            return em().createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User get(final String email, final String password) {
        try {
            return em().createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

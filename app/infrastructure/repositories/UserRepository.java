package infrastructure.repositories;


import core.objects.Page;
import domain.users.User;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Page<User> fetch(final int page, final int rows, final String q, final Boolean active) {
        String where = "WHERE 1 = 1";
        final Map<String, Object> params = new HashMap<>();

        if (q != null && !q.isEmpty()) {
            where += " AND (u.email LIKE :q OR u.name LIKE :q)";
            params.put("q", "%" + q + "%");
        }

        if (active == Boolean.TRUE) {
            where += " AND u.active";
        } else if (active == Boolean.FALSE) {
            where += " AND NOT u.active";
        }

        final TypedQuery<User> listQuery = em().createQuery("SELECT u FROM User u " + where + " ORDER BY u.email", User.class);
        final TypedQuery<Long> countQuery = em().createQuery("SELECT COUNT(u) FROM User u " + where, Long.class);

        params.forEach((k, v) -> {
            listQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        final Long total = countQuery.getSingleResult();
        final List<User> list = listQuery.setFirstResult((page - 1) * rows)
            .setMaxResults(rows)
            .getResultList();

        return new Page<>(page, rows, total, list);
    }

}

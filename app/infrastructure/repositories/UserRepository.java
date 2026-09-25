package infrastructure.repositories;


import com.querydsl.jpa.impl.JPAQuery;
import core.exceptions.AppException;
import core.objects.Page;
import domain.users.QUser;
import domain.users.User;

public class UserRepository extends JpaRepository<User, QUser, Long> {

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public UserRepository() {
        super(User.class, QUser.user);
    }

    public User get(final String email) {
        if (email == null) return null;
        return createQuery()
            .where(entity.email.eq(email))
            .fetchOne();
    }

    public Page<User> fetch(final int page, final int rows, final String q, final Boolean active) throws AppException {
        final QUser user = QUser.user;
        final JPAQuery<User> query = createQuery();

        if (q != null && !q.isEmpty()) {
            query.where(user.email.contains(q));
        }

        if (active == Boolean.TRUE) {
            query.where(user.active.isTrue());
        } else if (active == Boolean.FALSE) {
            query.where(user.active.isFalse());
        }

        return fetch(query, page, rows);
    }

}

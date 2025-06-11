package repositories;

import models.Order;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;

public class OrderRepository extends JpaRepository<Order, Long> {

    private static OrderRepository instance;

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public OrderRepository() {
        super(Order.class);
    }

    public Order getByNumber(final String number) {
        try {
            return JPA.em()
                .createQuery("SELECT o FROM Order o WHERE o.number = :number", Order.class)
                .setParameter("number", number)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

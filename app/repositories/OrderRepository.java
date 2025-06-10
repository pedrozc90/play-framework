package repositories;

import models.Order;

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

}

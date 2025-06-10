package repositories;

import models.OrderItem;

public class OrderItemRepository extends JpaRepository<OrderItem, Long> {

    private static OrderItemRepository instance;

    public static OrderItemRepository getInstance() {
        if (instance == null) {
            instance = new OrderItemRepository();
        }
        return instance;
    }

    public OrderItemRepository() {
        super(OrderItem.class);
    }

}

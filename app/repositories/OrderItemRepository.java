package repositories;

import models.Order;
import models.OrderItem;
import models.Product;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;

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

    public OrderItem get(final Product product, final Order order) {
        if (product == null || order == null) return null;
        try {
            return JPA.em()
                .createQuery("SELECT oi FROM OrderItem oi WHERE oi.product.id = :product_id AND oi.order.id = :order_id", OrderItem.class)
                .setParameter("product_id", product.getId())
                .setParameter("order_id", order.getId())
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

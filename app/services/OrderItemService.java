package services;

import controllers.objects.OrderItemDto;
import models.Order;
import models.OrderItem;
import models.Product;
import repositories.OrderItemRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderItemService implements EntityService<OrderItem> {

    private static OrderItemRepository repository = OrderItemRepository.getInstance();

    private static OrderItemService instance;

    public static OrderItemService getInstance() {
        if (instance == null) {
            instance = new OrderItemService();
        }
        return instance;
    }

    // QUERIES
    public OrderItem get(final Product product, final Order order) {
        return repository.get(product, order);
    }

    // METHODS
    @Override
    public void persist(final OrderItem entity) {
        repository.persist(entity);
    }

    @Override
    public OrderItem save(final OrderItem entity) {
        return repository.merge(entity);
    }

    @Override
    public void remove(final OrderItem entity) {
        repository.remove(entity);
    }

}

package services;

import controllers.objects.OrderItemDto;
import models.Order;
import models.OrderItem;
import repositories.OrderItemRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderItemService {

    private static OrderItemRepository repository = OrderItemRepository.getInstance();

    private static OrderItemService instance;

    public static OrderItemService getInstance() {
        if (instance == null) {
            instance = new OrderItemService();
        }
        return instance;
    }

    public OrderItem create(final String ean,
                            final Integer quantity,
                            final String labelType,
                            final String description,
                            final String size,
                            final Order order) {
        final OrderItem obj = new OrderItem();
        obj.setEan(ean);
        obj.setQuantity(quantity);
        obj.setLabelType(labelType);
        obj.setDescription(description);
        obj.setSize(size);
        obj.setOrder(order);
        return repository.persist(obj);
    }

    public Set<OrderItem> create(final Set<OrderItemDto> items, final Order order) {
        return items.stream().map((row) -> {
            final String size = Optional.ofNullable(row.getMetadata())
                .map(v -> v.get("size"))
                .map(Object::toString)
                .orElse(null);
            return create(row.getEan(), row.getQuantity(), row.getLabelType(), row.getDescription(), size, order);
        }).collect(Collectors.toSet());
    }

}

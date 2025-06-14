package services;

import actors.objects.ChangeLogEntry;
import actors.objects.Resolved;
import controllers.objects.OrderItemDto;
import controllers.objects.RequestItemDto;
import models.Order;
import models.OrderItem;
import models.Product;
import repositories.OrderItemRepository;

import java.util.*;
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

    @Override
    public OrderItem save(final OrderItem entity) {
        return repository.merge(entity);
    }

    @Override
    public void remove(final OrderItem entity) {
        repository.remove(entity);
    }

    @Override
    public void persist(final OrderItem entity) {
        repository.persist(entity);
    }

    // METHODS
    public Resolved<OrderItem> resolve(final RequestItemDto dto, final Product product, final Order order) {
        final OrderItem existing = repository.get(product, order);
        final OrderItem item = (existing != null) ? existing : new OrderItem();
        final boolean isNew = (existing == null);

        item.setProduct(product);
        item.setOrder(order);

        final List<ChangeLogEntry> changes = new ArrayList<>();

        final Integer quantity = dto.getQuantity();
        if (!Objects.equals(item.getQuantity(), quantity)) {
            if (item.getQuantity() != 0) {
                final ChangeLogEntry entry = ChangeLogEntry.changed("quantity", item.getQuantity(), quantity, dto.getEan());
                changes.add(entry);
            }
            item.setQuantity(quantity);
        }

        final String labelType = dto.getLabelType();
        if (!Objects.equals(item.getLabelType(), labelType)) {
            if (item.getLabelType() != null) {
                final ChangeLogEntry entry = ChangeLogEntry.changed("label_type", item.getLabelType(), labelType, dto.getEan());
                changes.add(entry);
            }
            item.setLabelType(labelType);
        }

        final boolean isCancelled = (item.getQuantity() == 0);
        if (item.isCancelled() != isCancelled) {
            final ChangeLogEntry entry = ChangeLogEntry.changed("cancelled", item.isCancelled(), isCancelled, dto.getEan());
            changes.add(entry);
            item.setCancelled(isCancelled);
        }

        if (isNew) {
            repository.persist(item);
        }

        return new Resolved<>(item, changes);
    }
}

package services;

import actors.objects.ChangeLogEntry;
import actors.objects.Resolved;
import mappers.OrderMapper;
import models.Order;
import models.Supplier;
import repositories.OrderRepository;

import java.util.ArrayList;
import java.util.Objects;

public class OrderService implements EntityService<Order> {

    private static OrderService instance;

    private static final OrderRepository repository = OrderRepository.getInstance();
    private static final OrderMapper orderMapper = OrderMapper.getInstance();
    private static final OrderItemService orderItemService = OrderItemService.getInstance();
    private static final SupplierService supplierService = SupplierService.getInstance();

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    // QUERIES
    public Order get(final Long id) {
        return repository.findById(id);
    }

    public Order get(final String number) {
        return repository.getByNumber(number);
    }

    @Override
    public Order save(final Order entity) {
        return repository.merge(entity);
    }

    @Override
    public void remove(final Order entity) {
        repository.remove(entity);
    }

    @Override
    public void persist(final Order entity) {
        repository.persist(entity);
    }

    // METHODS
    public Resolved<Order> resolve(final String number, final String status, final Supplier supplier) {
        final Order existing = repository.getByNumber(number);
        final Order order = existing != null ? existing : new Order();
        final boolean isNew = (existing == null);

        final ArrayList<ChangeLogEntry> changes = new ArrayList<>();

        if (isNew) {
            final ChangeLogEntry entry = ChangeLogEntry.added("number", null, number, "number");
            changes.add(entry);
            order.setNumber(number);
        }

        if (!Objects.equals(order.getStatus(), status)) {
            if (order.getStatus() != null) {
                final ChangeLogEntry entry = ChangeLogEntry.changed("status", order.getStatus(), status, "status");
                changes.add(entry);
            }
            order.setStatus(status);
        }

        if (order.getSupplier() == null) {
            final ChangeLogEntry entry = ChangeLogEntry.added("supplier", null, supplier.getCode(), "supplier");
            changes.add(entry);
            order.setSupplier(supplier);
        } else if (order.getSupplier() != supplier) {
            final ChangeLogEntry entry = ChangeLogEntry.changed("supplier", order.getSupplier().getCode(), supplier.getCode(), "supplier");
            changes.add(entry);
            order.setSupplier(supplier);
        }

        if (isNew) {
            repository.persist(order);
        }

        return new Resolved<>(order, changes);
    }

}

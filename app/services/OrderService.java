package services;

import mappers.OrderMapper;
import models.Order;
import models.Supplier;
import repositories.OrderRepository;

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

    // METHODS
    public Order create(final String number, final String status, final Supplier supplier) {
        final Order obj = new Order();
        obj.setStatus(status);
        obj.setNumber(number);
        obj.setSupplier(supplier);
        return repository.persist(obj);
    }

    @Override
    public void persist(final Order entity) {
        repository.persist(entity);
    }

    @Override
    public Order save(final Order entity) {
        return repository.merge(entity);
    }

    @Override
    public void remove(final Order entity) {
        repository.remove(entity);
    }

}

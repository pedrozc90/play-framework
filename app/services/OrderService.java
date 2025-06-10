package services;

import controllers.objects.OrderDto;
import controllers.objects.OrderItemDto;
import models.Order;
import models.OrderItem;
import repositories.OrderRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderService {

    private static OrderService instance;

    private static final OrderRepository repository = OrderRepository.getInstance();
    private static final OrderItemService orderItemService = OrderItemService.getInstance();

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public Order create(final String poClientNumber, final String supplierId, final String status) {
        final Order obj = new Order();
        obj.setStatus(status);
        obj.setNumber(poClientNumber);
        obj.setSupplier(supplierId);
        return repository.persist(obj);
    }

    public Order create(final OrderDto dto) {
        final String labelType = dto.getItems().stream()
            .map(OrderItemDto::getLabelType)
            .collect(Collectors.joining(", "));

        final Order order = create(dto.getNumber(), dto.getSupplier(), labelType);

        final Set<OrderItem> items = orderItemService.create(dto.getItems(), order);

        order.setItems(items);
        return order;
    }

}

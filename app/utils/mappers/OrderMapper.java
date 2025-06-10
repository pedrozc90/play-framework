package utils.mappers;

import controllers.objects.OrderDto;
import controllers.objects.OrderItemDto;
import models.Order;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderMapper implements EntityMapper<Order, OrderDto> {

    private static final OrderItemMapper itemMapper = OrderItemMapper.getInstance();

    private static OrderMapper instance;

    public static OrderMapper getInstance() {
        if (instance == null) {
            instance = new OrderMapper();
        }
        return instance;
    }

    @Override
    public OrderDto toDto(final Order order) {
        if (order == null) return null;

        final Set<OrderItemDto> items = order.getItems().stream().map(itemMapper::toDto).collect(Collectors.toSet());

        final OrderDto dto = new OrderDto();
        dto.setStatus(order.getStatus());
        dto.setNumber(order.getNumber());
        dto.setSupplier(order.getSupplier());
        dto.setItems(items);
        return dto;
    }

    @Override
    public Order toModel(final Order order, final OrderDto dto) {
        if (dto == null) return null;
        order.setStatus(dto.getStatus());
        order.setNumber(dto.getNumber());
        order.setSupplier(dto.getSupplier());
        return order;
    }

    @Override
    public Order toModel(final OrderDto dto) {
        return toModel(new Order(), dto);
    }

}

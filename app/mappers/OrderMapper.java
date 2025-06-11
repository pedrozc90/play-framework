package mappers;

import controllers.objects.OrderDto;
import controllers.objects.OrderItemDto;
import controllers.objects.SupplierDto;
import models.Order;
import models.Supplier;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderMapper implements EntityMapper<Order, OrderDto> {

    private static final OrderItemMapper itemMapper = OrderItemMapper.getInstance();
    private static final SupplierMapper supplierMapper = SupplierMapper.getInstance();

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
        final SupplierDto supplier = supplierMapper.toDto(order.getSupplier());

        final OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setNumber(order.getNumber());
        dto.setSupplier(supplier);
        dto.setItems(items);
        return dto;
    }

    @Override
    public Order toEntity(final Order order, final OrderDto dto) {
        if (dto == null) return null;

        final Supplier supplier = (order.getSupplier() != null)
            ? supplierMapper.toEntity(order.getSupplier(), dto.getSupplier())
            : supplierMapper.toEntity(dto.getSupplier());

        order.setId(dto.getId());
        order.setStatus(dto.getStatus());
        order.setNumber(dto.getNumber());
        order.setSupplier(supplier);
        return order;
    }

    @Override
    public Order toEntity(final OrderDto dto) {
        return toEntity(new Order(), dto);
    }

}

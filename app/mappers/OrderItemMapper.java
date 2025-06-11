package mappers;

import controllers.objects.OrderItemDto;
import controllers.objects.ProductDto;
import models.OrderItem;
import models.Product;

public class OrderItemMapper implements EntityMapper<OrderItem, OrderItemDto> {

    private static final ProductMapper productMapper = ProductMapper.getInstance();

    private static OrderItemMapper instance;

    public static OrderItemMapper getInstance() {
        if (instance == null) {
            instance = new OrderItemMapper();
        }
        return instance;
    }

    @Override
    public OrderItemDto toDto(final OrderItem entity) {
        if (entity == null) return null;

        final ProductDto product = productMapper.toDto(entity.getProduct());

        final OrderItemDto dto = new OrderItemDto();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setLabelType(entity.getLabelType());
        dto.setProduct(product);
        return dto;
    }

    @Override
    public OrderItem toEntity(final OrderItem entity, final OrderItemDto dto) {
        if (entity == null || dto == null) return null;

        final Product product = productMapper.toEntity(entity.getProduct(), dto.getProduct());

        entity.setId(dto.getId());
        entity.setQuantity(dto.getQuantity());
        entity.setLabelType(dto.getLabelType());
        entity.setProduct(product);
        return entity;
    }

    @Override
    public OrderItem toEntity(final OrderItemDto dto) {
        return toEntity(new OrderItem(), dto);
    }

}

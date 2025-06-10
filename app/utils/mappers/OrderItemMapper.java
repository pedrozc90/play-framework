package utils.mappers;

import controllers.objects.OrderItemDto;
import models.OrderItem;

import java.util.HashMap;
import java.util.Optional;

public class OrderItemMapper implements EntityMapper<OrderItem, OrderItemDto> {

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
        final OrderItemDto dto = new OrderItemDto();
        dto.setEan(entity.getEan());
        dto.setQuantity(entity.getQuantity());
        dto.setDescription(entity.getDescription());
        dto.setLabelType(entity.getLabelType());
        dto.setMetadata(new HashMap<>());
        dto.getMetadata().put("size", entity.getSize());
        return dto;
    }

    @Override
    public OrderItem toModel(final OrderItem entity, final OrderItemDto dto) {
        if (entity == null || dto == null) return null;
        entity.setEan(dto.getEan());
        entity.setQuantity(dto.getQuantity());
        entity.setDescription(dto.getDescription());
        entity.setLabelType(dto.getLabelType());
        Optional.ofNullable(dto.getMetadata())
            .map(v -> v.get("size"))
            .ifPresent(v -> entity.setSize(v.toString()));
        return entity;
    }

    @Override
    public OrderItem toModel(final OrderItemDto dto) {
        return toModel(new OrderItem(), dto);
    }

}

package utils.mappers;

import controllers.objects.PurchaseOrderDto;
import models.PurchaseOrder;

public class PurchaseOrderMapper implements EntityMapper<PurchaseOrder, PurchaseOrderDto> {

    private static final OrderMapper mapper = OrderMapper.getInstance();

    private static PurchaseOrderMapper instance;

    public static PurchaseOrderMapper getInstance() {
        if (instance == null) {
            instance = new PurchaseOrderMapper();
        }
        return instance;
    }

    @Override
    public PurchaseOrderDto toDto(final PurchaseOrder entity) {
        if (entity == null) return null;

        final PurchaseOrderDto dto = new PurchaseOrderDto();
        dto.setHash(entity.getHash());
        dto.setNumber(entity.getNumber());
        dto.setStatus(entity.getStatus());
        dto.setOrder(mapper.toDto(entity.getOrder()));
        return dto;
    }

    @Override
    public PurchaseOrder toModel(final PurchaseOrder entity, final PurchaseOrderDto dto) {
        if (dto == null) return null;
        entity.setHash(dto.getHash());
        entity.setNumber(dto.getNumber());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    @Override
    public PurchaseOrder toModel(final PurchaseOrderDto dto) {
        return toModel(new PurchaseOrder(), dto);
    }

}

package mappers;

import actors.objects.ChangeLogDto;
import controllers.objects.PurchaseOrderDto;
import models.PurchaseOrder;

public class PurchaseOrderMapper implements EntityMapper<PurchaseOrder, PurchaseOrderDto> {

    private static final OrderMapper orderMapper = OrderMapper.getInstance();
    private static final ChangeLogMapper changeLogMapper = ChangeLogMapper.getInstance();

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

        final ChangeLogDto changelog = changeLogMapper.toDto(entity.getChangelog());

        final PurchaseOrderDto dto = new PurchaseOrderDto();
        dto.setId(entity.getId());
        dto.setHash(entity.getHash());
        dto.setNumber(entity.getNumber());
        dto.setStatus(entity.getStatus());
        dto.setChangelog(changelog);
        dto.setOrder(orderMapper.toDto(entity.getOrder()));
        return dto;
    }

    @Override
    public PurchaseOrder toEntity(final PurchaseOrder entity, final PurchaseOrderDto dto) {
        if (dto == null) return null;
        entity.setId(dto.getId());
        entity.setHash(dto.getHash());
        entity.setNumber(dto.getNumber());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    @Override
    public PurchaseOrder toEntity(final PurchaseOrderDto dto) {
        return toEntity(new PurchaseOrder(), dto);
    }

}

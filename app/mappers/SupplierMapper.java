package mappers;

import controllers.objects.SupplierDto;
import models.Supplier;

public class SupplierMapper implements EntityMapper<Supplier, SupplierDto> {

    private static SupplierMapper instance;

    public static SupplierMapper getInstance() {
        if (instance == null) {
            instance = new SupplierMapper();
        }
        return instance;
    }

    @Override
    public SupplierDto toDto(final Supplier entity) {
        if (entity == null) return null;

        final SupplierDto dto = new SupplierDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        return dto;
    }

    @Override
    public Supplier toEntity(final Supplier entity, final SupplierDto dto) {
        if (dto == null) return null;
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        return entity;
    }

    @Override
    public Supplier toEntity(final SupplierDto dto) {
        return toEntity(new Supplier(), dto);
    }

}

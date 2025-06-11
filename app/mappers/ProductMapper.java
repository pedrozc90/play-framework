package mappers;

import controllers.objects.ProductDto;
import models.Product;

public class ProductMapper implements EntityMapper<Product, ProductDto> {

    private static ProductMapper instance;

    public static ProductMapper getInstance() {
        if (instance == null) {
            instance = new ProductMapper();
        }
        return instance;
    }

    @Override
    public ProductDto toDto(final Product entity) {
        if (entity == null) return null;

        final ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setEan(entity.getEan());
        dto.setDescription(entity.getDescription());
        dto.setSize(entity.getSize());
        dto.setColor(entity.getColor());
        return dto;
    }

    @Override
    public Product toEntity(final Product entity, final ProductDto dto) {
        if (dto == null) return null;
        entity.setId(dto.getId());
        entity.setEan(dto.getEan());
        entity.setDescription(dto.getDescription());
        entity.setSize(dto.getSize());
        entity.setColor(dto.getColor());
        return entity;
    }

    @Override
    public Product toEntity(final ProductDto dto) {
        return toEntity(new Product(), dto);
    }

}

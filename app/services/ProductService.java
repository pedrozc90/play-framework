package services;

import actors.objects.ChangeLogEntry;
import actors.objects.Resolved;
import controllers.objects.RequestItemDto;
import models.Product;
import repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductService implements EntityService<Product> {

    private static ProductService instance;

    private static final ProductRepository repository = ProductRepository.getInstance();

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    // QUERIES
    public Product get(final String ean) {
        return repository.getByEan(ean);
    }

    @Override
    public void persist(final Product entity) {
        repository.persist(entity);
    }

    @Override
    public Product save(final Product entity) {
        return repository.merge(entity);
    }

    @Override
    public void remove(final Product entity) {
        repository.remove(entity);
    }

    // METHODS
    public Resolved<Product> resolve(final RequestItemDto dto) {
        final Product existing = repository.getByEan(dto.getEan());
        final Product product = existing != null ? existing : new Product();
        final boolean isNew = (existing == null);

        final List<ChangeLogEntry> changes = new ArrayList<>();

        final String ean = dto.getEan();
        if (isNew) {
            final ChangeLogEntry entry = ChangeLogEntry.of(ChangeLogEntry.Type.ADDED, "ean", null, ean, "ean");
            changes.add(entry);
        }
        product.setEan(ean);

        final String description = dto.getDescription();
        if (!Objects.equals(product.getDescription(), description)) {
            final ChangeLogEntry entry = ChangeLogEntry.of(ChangeLogEntry.Type.CHANGED, "description", product.getDescription(), description, "ean");
            changes.add(entry);
        }
        product.setDescription(description);

        final Map<String, Object> metadata = dto.getMetadata();

        final String size = metadata != null ? Objects.toString(metadata.get("size"), null) : null;
        if (!Objects.equals(product.getSize(), size)) {
            // final ChangeLogEntry.Type type = isNew ? ChangeLogEntry.Type.ADDED : ChangeLogEntry.Type.CHANGED;
            final ChangeLogEntry entry = ChangeLogEntry.changed( "size", product.getSize(), size, ean);
            changes.add(entry);
        }
        product.setSize(size);

        final String color = metadata != null ? Objects.toString(metadata.get("color"), null) : null;
        if (!Objects.equals(product.getColor(), color)) {
            // final ChangeLogEntry.Type type = isNew ? ChangeLogEntry.Type.ADDED : ChangeLogEntry.Type.CHANGED;
            final ChangeLogEntry entry = ChangeLogEntry.changed( "color", product.getColor(), color, ean);
            changes.add(entry);
        }
        product.setColor(color);

        if (isNew) {
            repository.persist(product);
        }

        return new Resolved<>(product, changes);
    }

}

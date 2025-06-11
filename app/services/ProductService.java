package services;

import models.Product;
import repositories.ProductRepository;

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
        return repository.getByCode(ean);
    }

    // METHODS
    public Product create(final String ean, final String description, final String size, final String color) {
        final Product obj = new Product();
        obj.setEan(ean);
        obj.setDescription(description);
        obj.setSize(size);
        obj.setColor(color);
        return repository.persist(obj);
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

}

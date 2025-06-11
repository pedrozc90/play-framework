package services;

import models.Supplier;
import repositories.SupplierRepository;

public class SupplierService implements EntityService<Supplier> {

    private static SupplierService instance;

    private static final SupplierRepository repository = SupplierRepository.getInstance();

    public static SupplierService getInstance() {
        if (instance == null) {
            instance = new SupplierService();
        }
        return instance;
    }

    // QUERIES
    public Supplier get(final String code) {
        return repository.getByCode(code);
    }

    // METHODS
    public Supplier create(final String code) {
        final Supplier obj = new Supplier();
        obj.setCode(code);
        return repository.persist(obj);
    }

    @Override
    public void persist(final Supplier entity) {
        repository.persist(entity);
    }

    @Override
    public Supplier save(final Supplier entity) {
        return repository.merge(entity);
    }

    @Override
    public void remove(final Supplier entity) {
        repository.remove(entity);
    }

}

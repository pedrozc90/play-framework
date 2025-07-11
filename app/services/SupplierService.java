package services;

import actors.objects.ChangeLogEntry;
import actors.objects.Resolved;
import models.Supplier;
import repositories.SupplierRepository;

import java.util.ArrayList;
import java.util.List;

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

    // METHODS
    public Supplier create(final String code) {
        final Supplier obj = new Supplier();
        obj.setCode(code);
        return repository.persist(obj);
    }

    public Resolved<Supplier> resolve(final String code) {
        final Supplier existing = repository.getByCode(code);
        final Supplier supplier = existing != null ? existing : new Supplier();
        final boolean isNew = existing == null;

        final List<ChangeLogEntry> changes = new ArrayList<>();

        if (isNew) {
            final ChangeLogEntry entry = ChangeLogEntry.added("code", null, code, "supplier");
            changes.add(entry);
            supplier.setCode(code);
            repository.persist(supplier);
        }

        return new Resolved<>(supplier, changes);
    }

}

package repositories;

import models.Supplier;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;

public class SupplierRepository extends JpaRepository<Supplier, Long> {

    private static SupplierRepository instance;

    public static SupplierRepository getInstance() {
        if (instance == null) {
            instance = new SupplierRepository();
        }
        return instance;
    }

    public SupplierRepository() {
        super(Supplier.class);
    }

    public Supplier getByCode(final String code) {
        try {
            return JPA.em()
                .createQuery("SELECT s FROM Supplier s WHERE s.code = :code", Supplier.class)
                .setParameter("code", code)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

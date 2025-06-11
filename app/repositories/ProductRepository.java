package repositories;

import models.Product;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;

public class ProductRepository extends JpaRepository<Product, Long> {

    private static ProductRepository instance;

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public ProductRepository() {
        super(Product.class);
    }

    public Product getByCode(final String ean) {
        try {
            return JPA.em()
                .createQuery("SELECT p FROM Product p WHERE p.ean = :ean", clazz)
                .setParameter("ean", ean)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

package repositories;

import models.PurchaseOrder;
import play.db.jpa.JPA;

import javax.persistence.LockModeType;
import java.util.List;

public class PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    private static PurchaseOrderRepository instance;

    public static PurchaseOrderRepository getInstance() {
        if (instance == null) {
            instance = new PurchaseOrderRepository();
        }
        return instance;
    }

    public PurchaseOrderRepository() {
        super(PurchaseOrder.class);
    }

    public List<PurchaseOrder> findByStatus(final PurchaseOrder.Status status) {
        return JPA.em()
            .createQuery("SELECT po FROM PurchaseOrder po WHERE po.status = :status ORDER BY po.id ASC", PurchaseOrder.class)
            .setParameter("status", status)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE) // lock mode works only in entities, tuples do not.
            .getResultList();
    }

}

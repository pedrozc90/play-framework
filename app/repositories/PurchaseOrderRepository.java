package repositories;

import models.PurchaseOrder;
import play.db.jpa.JPA;

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

    public List<PurchaseOrder> findWaitingOrders() {
        return JPA.em()
            .createQuery("SELECT po FROM PurchaseOrder po WHERE po.status = :status", PurchaseOrder.class)
            .setParameter("status", "WAITING")
            .getResultList();
    }

}

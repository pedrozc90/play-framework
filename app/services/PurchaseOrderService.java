package services;

import models.PurchaseOrder;
import repositories.PurchaseOrderRepository;
import utils.HashUtils;

import java.util.List;

public class PurchaseOrderService implements EntityService<PurchaseOrder> {

    private static PurchaseOrderRepository repository = PurchaseOrderRepository.getInstance();

    private static final HashUtils hashUtils = HashUtils.getInstance();

    private static PurchaseOrderService instance;

    public static PurchaseOrderService getInstance() {
        if (instance == null) {
            instance = new PurchaseOrderService();
        }
        return instance;
    }

    // QUERIES
    public PurchaseOrder get(final Long id) {
        return repository.findById(id);
    }

    public List<PurchaseOrder> findWaitingOrders() {
        return repository.findByStatus(PurchaseOrder.Status.WAITING);
    }

    public List<PurchaseOrder> findOngoing() {
        return repository.findByStatus(PurchaseOrder.Status.ONGOING);
    }

    // METHODS
    public PurchaseOrder create(final String number, final String content) {
        final String hash = hashUtils.md5("debug");

        final PurchaseOrder obj = new PurchaseOrder();
        obj.setHash(hash);
        obj.setStatus(PurchaseOrder.Status.WAITING);
        obj.setNumber(number);
        obj.setContent(content);
        return repository.persist(obj);
    }

    public void updateToOngoing(final PurchaseOrder purchaseOrder) {
        purchaseOrder.setStatus(PurchaseOrder.Status.ONGOING);
        repository.merge(purchaseOrder);
    }

    public void updateToDone(final PurchaseOrder purchaseOrder) {
        purchaseOrder.setStatus(PurchaseOrder.Status.DONE);
        repository.merge(purchaseOrder);
    }

    @Override
    public void persist(final PurchaseOrder entity) {
        repository.persist(entity);
    }

    @Override
    public PurchaseOrder save(final PurchaseOrder entity) {
        return null;
    }

    @Override
    public void remove(final PurchaseOrder entity) {

    }
}

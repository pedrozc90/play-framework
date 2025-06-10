package services;

import models.PurchaseOrder;
import repositories.PurchaseOrderRepository;
import utils.HashUtils;

public class PurchaseOrderService {

    private static PurchaseOrderRepository repository = PurchaseOrderRepository.getInstance();

    private static final HashUtils hashUtils = HashUtils.getInstance();

    private static PurchaseOrderService instance;

    public static PurchaseOrderService getInstance() {
        if (instance == null) {
            instance = new PurchaseOrderService();
        }
        return instance;
    }

    public PurchaseOrder create(final String number, final String content) {
        final String hash = hashUtils.md5("debug");

        final PurchaseOrder obj = new PurchaseOrder();
        obj.setHash(hash);
        obj.setStatus("WAITING");
        obj.setNumber(number);
        obj.setContent(content);
        return repository.persist(obj);
    }

}

package repositories.tuples;

import models.PurchaseOrder;

import javax.persistence.Column;
import java.util.Objects;

public class PurchaseOrderTuple {

    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    public PurchaseOrderTuple(final Long id, final String number) {
        this.id = id;
        this.number = number;
    }

    public PurchaseOrderTuple(final PurchaseOrder entity) {
        this(entity.getId(), entity.getNumber());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderTuple that = (PurchaseOrderTuple) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

}

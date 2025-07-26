package repositories.tuples;

import lombok.AllArgsConstructor;
import lombok.Data;
import models.PurchaseOrder;

import javax.persistence.Column;
import java.util.Objects;

@Data
@AllArgsConstructor
public class PurchaseOrderTuple {

    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    public static PurchaseOrderTuple of(final PurchaseOrder entity) {
        return new PurchaseOrderTuple(entity.getId(), entity.getNumber());
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

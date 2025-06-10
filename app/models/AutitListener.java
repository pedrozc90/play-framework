package models;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;

public class AutitListener {

    @PrePersist
    public void onInsert(final AuditEntity entity) {
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        entity.setInsertedAt(now);
    }

    @PreUpdate
    public void onUpdate(final AuditEntity entity) {
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        entity.setUpdatedAt(now);
    }

}

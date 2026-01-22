package domain.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;

public class AuditListener {

    @PrePersist
    public void onInsert(final AuditEntity entity) {
        final Timestamp now = currentTimestamp();
        entity.setInsertedAt(now);
        entity.setUpdatedAt(now);
    }

    @PreUpdate
    public void onUpdate(final AuditEntity entity) {
        final Timestamp now = currentTimestamp();
        entity.setUpdatedAt(now);
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

}

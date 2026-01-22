package domain.audit;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(value = { AuditListener.class })
public abstract class AuditEntity implements Serializable {

    @Column(name = "uuid", length = 36, nullable = false, updatable = false)
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "inserted_at", nullable = false, updatable = false)
    private Timestamp insertedAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 0;

}

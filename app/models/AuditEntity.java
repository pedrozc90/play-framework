package models;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(value = { AutitListener.class })
public abstract class AuditEntity implements Serializable {

    @Column(name = "inserted_at", nullable = false)
    private Timestamp insertedAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 0;

    @Column(name = "uuid", length = 36, nullable = false, updatable = false)
    private String uuid = UUID.randomUUID().toString();

    public Timestamp getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Timestamp insertedAt) {
        this.insertedAt = insertedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}

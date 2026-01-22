package domain.jobs;

import domain.audit.AuditEntity;
import domain.files.FileStorage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(
    name = "jobs",
    uniqueConstraints = @UniqueConstraint(name = "jobs_uuid_key", columnNames = "uuid")
)
public class Job extends AuditEntity implements Serializable {

    public enum Status {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 64, nullable = false)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileStorage file;

}

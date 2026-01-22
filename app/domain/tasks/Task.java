package domain.tasks;

import domain.audit.AuditEntity;
import domain.jobs.Job;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(
    name = "tasks",
    uniqueConstraints = @UniqueConstraint(name = "tasks_uuid_ukey", columnNames = "uuid")
)
public class Task extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32, nullable = false)
    private TaskType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, nullable = false)
    private TaskStatus status;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "started_at")
    private Timestamp startedAt;

    @Column(name = "completed_at")
    private Timestamp completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

}

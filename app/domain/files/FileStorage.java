package domain.files;

import domain.audit.AuditEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(
    name = "file_storage",
    uniqueConstraints = {
        @UniqueConstraint(name = "file_storage_uuid_ukey", columnNames = { "uuid" })
    }
)
public class FileStorage extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "hash", length = 64, nullable = false, updatable = false)
    private String hash;

    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Column(name = "content", nullable = false)
    private byte[] content;

    @NotNull
    @Column(name = "content_type", nullable = false)
    private String contentType = "application/octet-stream";

    @NotNull
    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "charset")
    private String charset;

    @NotNull
    @Column(name = "length", nullable = false)
    private long length = 0L;

}

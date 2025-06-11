package models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(
    name = "suppliers",
    uniqueConstraints = @UniqueConstraint(name = "suppliers_code_key", columnNames = "code")
)
public class Supplier extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", length = 32, nullable = false, unique = true)
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

}

package domain.users;

import domain.audit.AuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "users_uuid_ukey", columnNames = "uuid"),
        @UniqueConstraint(name = "users_email_ukey", columnNames = "email")
    }
)
public class User extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active = true;

}

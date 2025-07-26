package models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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

}

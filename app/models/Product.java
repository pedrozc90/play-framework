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
    name = "products",
    uniqueConstraints = @UniqueConstraint(name = "products_ean_key", columnNames = "ean")
)
public class Product extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ean", length = 32, nullable = false, unique = true)
    private String ean;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "size", length = 64)
    private String size;

    @Column(name = "color", length = 64)
    private String color;

}

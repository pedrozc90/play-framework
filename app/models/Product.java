package models;

import javax.persistence.*;
import java.io.Serializable;

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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(final String ean) {
        this.ean = ean;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

}

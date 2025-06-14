package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends AuditEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Status {
        WAITING,
        ONGOING,
        DONE,
        ERROR;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number", length = 32, nullable = false)
    private String number;

    @Column(name = "hash", length = 32, nullable = false)
    private String hash;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, nullable = false)
    private Status status = Status.WAITING;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "changelog", columnDefinition = "TEXT")
    private String changelog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}

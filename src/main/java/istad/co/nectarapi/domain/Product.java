package istad.co.nectarapi.domain;

import istad.co.nectarapi.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    private Boolean isActive;

    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

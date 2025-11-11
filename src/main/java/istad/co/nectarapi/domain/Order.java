package istad.co.nectarapi.domain;

import istad.co.nectarapi.audit.Auditable;
import istad.co.nectarapi.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}

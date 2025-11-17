package istad.co.nectarapi.features.orderItem;

import istad.co.nectarapi.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

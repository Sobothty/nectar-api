package istad.co.nectarapi.features.order;

import istad.co.nectarapi.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByUuid(String uuid);
    Optional<Order> findByUuid(String uuid);

    @Modifying
    @Query("UPDATE Order o SET o.isDeleted = true WHERE o.uuid = ?1")
    void deleteSoftByUuid(String uuid);
}
